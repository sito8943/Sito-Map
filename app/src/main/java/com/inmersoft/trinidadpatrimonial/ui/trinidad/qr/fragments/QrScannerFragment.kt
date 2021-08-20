package com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.fragments

import android.Manifest
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.AlertDialog
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.transition.Hold
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.FragmentQrScannerBinding
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.barcodedetection.BarcodeProcessor
import com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.camera.CameraSource
import com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.camera.CameraSourcePreview
import com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.camera.GraphicOverlay
import com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.camera.WorkflowModel
import com.inmersoft.trinidadpatrimonial.utils.TrinidadQR
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException
import java.util.*

@AndroidEntryPoint
class QrScannerFragment : BaseFragment(), View.OnClickListener,
    EasyPermissions.PermissionCallbacks {
    private var cameraSource: CameraSource? = null
    private var preview: CameraSourcePreview? = null
    private var graphicOverlay: GraphicOverlay? = null
    private var settingsButton: View? = null
    private var flashButton: View? = null
    private var promptChip: Chip? = null
    private var promptChipAnimator: AnimatorSet? = null
    private var currentWorkflowState: WorkflowModel.WorkflowState? = null
    private val workflowModel: WorkflowModel by viewModels()
    private lateinit var binding: FragmentQrScannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentQrScannerBinding.inflate(layoutInflater, container, false)

        preview = binding.cameraPreview

        graphicOverlay =
            binding.root.findViewById<GraphicOverlay>(R.id.camera_preview_graphic_overlay).apply {
                setOnClickListener(this@QrScannerFragment)
                cameraSource = CameraSource(this)
            }

        promptChip = binding.root.findViewById(R.id.bottom_prompt_chip)

        promptChipAnimator =
            (AnimatorInflater.loadAnimator(
                requireContext(),
                R.animator.bottom_prompt_chip_enter
            ) as AnimatorSet).apply {
                setTarget(promptChip)
            }

        binding.root.findViewById<View>(R.id.close_button).setOnClickListener(this)

        flashButton = binding.root.findViewById<View>(R.id.flash_button).apply {
            setOnClickListener(this@QrScannerFragment)
        }


        if (!hasCameraPermission()) {
            requestCameraPermission()
        }

        setUpWorkflowModel()

        setupBottomSheet()

        return binding.root
    }

    private fun setupBottomSheet() {
        trinidadBottomSheet =
            TrinidadBottomSheet(
                requireContext(),
                started = false,
                binding.root as ViewGroup,
                findNavController()
            )
        trinidadBottomSheet.addTrindadBottomSheetCallBack(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        startCameraPreview()
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
//BottomSheet Information
        trinidadDataViewModel.currentPlaceToBottomSheet.observe(viewLifecycleOwner,
            { currentPlace ->
                Log.d(TAG, "setupBottomSheet: OBSERVE COLLECTING INFO")
                Log.d(TAG, "setupBottomSheet: PLACEDATA-> ${currentPlace?.place_name}")

                if (trinidadDataViewModel.isParent(TAG)) {
                    Log.d(TAG, "setupBottomSheet: PARENT IS CORRECT ")
                    if (currentPlace != null) {
                        Log.d(TAG, "setupBottomSheet: CURRENTPLACE IS NOT NULL")
                        val nav = QrScannerFragmentDirections.actionNavQrToDetailsFragment(
                            currentPlace.place_id
                        )
                        Log.d(TAG,
                            "setupBottomSheet: CALLING showTrinidadBottomSheetPlaceInfo function")
                        showTrinidadBottomSheetPlaceInfo(
                            place = currentPlace, navDirections = nav
                        )
                    }
                } else {
                    Log.d(TAG, "setupBottomSheet: PARENT IS NOT CORRECT ")
                }
            })

    }

    override fun onResume() {
        super.onResume()

        workflowModel?.markCameraFrozen()
        settingsButton?.isEnabled = true
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED
        cameraSource?.setFrameProcessor(BarcodeProcessor(graphicOverlay!!, workflowModel!!))
        workflowModel?.setWorkflowState(WorkflowModel.WorkflowState.DETECTING)
    }

    override fun onPause() {
        super.onPause()
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED
        stopCameraPreview()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource?.release()
        cameraSource = null
    }

    private fun onBackPressed() {
        findNavController().navigateUp()
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.close_button -> onBackPressed()
            R.id.flash_button -> {
                flashButton?.let {
                    if (it.isSelected) {
                        it.isSelected = false
                        cameraSource?.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF)
                    } else {
                        it.isSelected = true
                        cameraSource!!.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
                    }
                }
            }

        }
    }

    private fun startCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        val cameraSource = this.cameraSource ?: return
        if (!workflowModel.isCameraLive) {
            try {
                workflowModel.markCameraLive()
                preview?.start(cameraSource)
            } catch (e: IOException) {
                Log.e("TAG", "Failed to start camera preview!", e)
                cameraSource.release()
                this.cameraSource = null
            }
        }
    }

    private fun stopCameraPreview() {
        val workflowModel = this.workflowModel ?: return
        if (workflowModel.isCameraLive) {
            workflowModel.markCameraFrozen()
            flashButton?.isSelected = false
            preview?.stop()
        }
    }


    private fun setUpWorkflowModel() {
        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel!!.workflowState.observe(viewLifecycleOwner, { workflowState ->
            if (workflowState != null || !Objects.equals(currentWorkflowState, workflowState)) {

                currentWorkflowState = workflowState
                Log.d("TAG", "Current workflow state: ${currentWorkflowState!!.name}")

                val wasPromptChipGone = promptChip?.visibility == View.GONE

                when (workflowState) {
                    WorkflowModel.WorkflowState.DETECTING -> {
                        promptChip?.visibility = View.VISIBLE
                        promptChip?.setText(R.string.prompt_point_at_a_barcode)
                        startCameraPreview()
                    }
                    WorkflowModel.WorkflowState.CONFIRMING -> {
                        promptChip?.visibility = View.VISIBLE
                        promptChip?.setText(R.string.prompt_move_camera_closer)
                        startCameraPreview()
                    }
                    WorkflowModel.WorkflowState.SEARCHING -> {
                        promptChip?.visibility = View.VISIBLE
                        promptChip?.setText(R.string.prompt_searching)
                        stopCameraPreview()
                    }
                    WorkflowModel.WorkflowState.DETECTED, WorkflowModel.WorkflowState.SEARCHED -> {
                        promptChip?.visibility = View.GONE
                        stopCameraPreview()
                    }
                    else -> promptChip?.visibility = View.GONE
                }

                val shouldPlayPromptChipEnteringAnimation =
                    wasPromptChipGone && promptChip?.visibility == View.VISIBLE
                promptChipAnimator?.let {
                    if (shouldPlayPromptChipEnteringAnimation && !it.isRunning) it.start()
                }
            }
        })

        workflowModel.detectedBarcode.observe(viewLifecycleOwner, { barcode ->
            if (barcode != null) {
                Log.d(TAG, "setUpWorkflowModel: ${barcode.rawValue}")
                processBarcodeValue(barcode.rawValue!!)
            }
        })
    }

    private fun processBarcodeValue(barcodeRaw: String) {
        val trinidadQR = TrinidadQR(barcodeRaw)
        if (trinidadQR.isValid()) {
            Log.d(TAG, "processBarcodeValue: QR IS VALID")
            val placeID = trinidadQR.getPlaceID()
            if (placeID >= 0) {
                Log.d(TAG, "processBarcodeValue: SEND DATA TO BOTTOMSHEET")
                trinidadDataViewModel.onBottomSheetSetInfo(placeId = placeID, _parent =
                TAG)
            }
        } else {
            Log.d(TAG, "processBarcodeValue: QR IS NOT VALID")
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(resources.getString(R.string.app_name))
            builder.setIcon(R.drawable.ic_menu_qr)
            builder.setMessage(resources.getString(R.string.not_valid_qrcode))
            builder.setPositiveButton(resources.getString(R.string.OK)) { dialog, which ->
                startCameraPreview()
                dialog.dismiss()
            }
            builder.show()
        }
    }

    private fun hasCameraPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun requestCameraPermission() {
        EasyPermissions.requestPermissions(
            this,
            resources.getString(R.string.camera_rationale_info), CAMERA_PERMISSION_CODE,
            Manifest.permission.CAMERA
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestCameraPermission()
        }

    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
/*
        if (requestCode == CAMERA_PERMISSION_CODE) {

        }*/
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)

    }

    companion object {
        const val CAMERA_PERMISSION_CODE = 5637
        const val TAG = "QRScanFragment"
    }
}