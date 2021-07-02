package com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.ProcessCameraProvider.getInstance
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.inmersoft.trinidadpatrimonial.databinding.FragmentQrScannerBinding
import com.inmersoft.trinidadpatrimonial.ui.BaseFragment
import com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.detection.QrProcessor
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QrScannerFragment : BaseFragment(), QrProcessor.IScanProcessListener,EasyPermissions.PermissionCallbacks {
    private lateinit var cameraPreview: PreviewView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private var camera: Camera? = null
    private lateinit var qrProcessor: QrProcessor

    private lateinit var binding: FragmentQrScannerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQrScannerBinding.inflate(inflater, container, false)
        cameraPreview = binding.cameraView
        cameraProviderFuture = getInstance(requireContext())
        qrProcessor = QrProcessor(binding.cameraOverlay, this)

        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, {
            Log.d(TAG, "onCreateView: ${it.size}")
        })

        if (!hasCameraPermission()) {
            requestCameraPermission()
        } else {
            startCamera()
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

        trinidadBottomSheet = TrinidadBottomSheet(
            requireContext(),
            started = false,
            binding.root as ViewGroup,
            findNavController()
        )

        trinidadDataViewModel.currentPlaceToBottomSheet.observe(viewLifecycleOwner,
            { currentPlace ->
                if (currentPlace != null) {
                    val nav = QrScannerFragmentDirections.actionNavQrToDetailsFragment(
                        currentPlace.place_id
                    )
                    showTrinidadBottomSheetPlaceInfo(
                        place = currentPlace, navDirections = nav
                    )
                }
            })


        return binding.root
    }

    private fun startCamera() {
        val fragmentContext = requireContext()
        cameraProviderFuture = getInstance(fragmentContext)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindUseCases(cameraProvider)
        }, ContextCompat.getMainExecutor(fragmentContext))
    }

    private fun bindUseCases(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder().build().apply {
            setSurfaceProvider(cameraPreview.surfaceProvider)
        }

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), qrProcessor)

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        camera = cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )
    }
    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onDetected(scanResult: String?) {

        if (!scanResult.isNullOrEmpty() && scanResult.isDigitsOnly()) {
            val placeID = scanResult.toInt()
            trinidadDataViewModel.onBottomSheetShow(placeID)
        }
    }


    private fun hasCameraPermission() =
        EasyPermissions.hasPermissions(requireContext(), Manifest.permission.CAMERA)

    private fun requestCameraPermission() {
        EasyPermissions.requestPermissions(
            this,
            " Mensnaje sobre la cmara ", CAMERA_PERMISSION_CODE,
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
        if (requestCode == CAMERA_PERMISSION_CODE) {
            startCamera()
        }
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
        private const val TAG = "QrScannerFragment"
        private const val CAMERA_PERMISSION_CODE = 5637
    }
}
