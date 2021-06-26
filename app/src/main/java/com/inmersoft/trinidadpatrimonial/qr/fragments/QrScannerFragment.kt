package com.inmersoft.trinidadpatrimonial.qr.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.ProcessCameraProvider.getInstance
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.common.util.concurrent.ListenableFuture
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.databinding.QrScannerFragmentBinding
import com.inmersoft.trinidadpatrimonial.qr.qrdetection.QrProcessor
import com.inmersoft.trinidadpatrimonial.utils.ShareIntent
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.TrinidadCustomChromeTab
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QrScannerFragment : Fragment(), QrProcessor.IScanProcessListener {
    private lateinit var cameraPreview: PreviewView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private var camera: Camera? = null
    private lateinit var qrProcessor: QrProcessor

    private lateinit var binding: QrScannerFragmentBinding

    private lateinit var bottomSheet: BottomSheetBehavior<ConstraintLayout>

    private val trinidadDataViewModel: TrinidadDataViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = QrScannerFragmentBinding.inflate(inflater, container, false)
        cameraPreview = binding.cameraView
        cameraProviderFuture = getInstance(requireContext())
        qrProcessor = QrProcessor(binding.cameraOverlay, this)

        trinidadDataViewModel.allPlaces.observe(viewLifecycleOwner, {
            Log.d(TAG, "onCreateView: ${it.size}")
        })

        if (isPermissionGranted())
            startCamera()
        else
            requestPermission.launch(REQUIRED_PERMISSION)

        cameraExecutor = Executors.newSingleThreadExecutor()

        bottomSheet = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

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

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted)
                startCamera()
            else {
                findNavController().popBackStack()
            }
        }

    private fun isPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(), REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onDetected(scanResult: String?) {
        if (!scanResult.isNullOrEmpty() && scanResult.isDigitsOnly()) {
            val placeID = scanResult.toInt()
            lifecycleScope.launch(Dispatchers.IO)
            {
                Log.d("TAG", "onMarkerClick: $placeID")
                val place = trinidadDataViewModel.getPlaceById(placeID)
                lifecycleScope.launch(Dispatchers.Main) {
                    Glide.with(requireContext())
                        .asBitmap()
                        .load(Uri.parse(TrinidadAssets.getAssetFullPath(place.header_images[0])))
                        .placeholder(R.drawable.placeholder_error)
                        .error(R.drawable.placeholder_error)
                        .into(object : CustomTarget<Bitmap>() {
                            override fun onResourceReady(
                                resource: Bitmap,
                                transition: Transition<in Bitmap>?
                            ) {
                                binding.qrBottomSheetImageHeader.setImageBitmap(resource)
                                binding.bottomSheetShare.setOnClickListener {
                                    ShareIntent.shareIt(
                                        requireContext(),
                                        resource,
                                        place.place_name,
                                        getString(R.string.app_name)
                                    )
                                }
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {

                            }

                        })

                    binding.bottomSheetPlaceName.text = place.place_name
                    binding.bottomSheetPlaceName.isSelected = true
                    binding.bottomSheetPlaceDescription.text = place.place_description
                    binding.bottomSheetWebpage.setOnClickListener {
                        TrinidadCustomChromeTab.launch(requireContext(), place.web)
                    }
                    binding.seeMoreButton.setOnClickListener {
                        binding.bottomSheetImage.transitionName = UUID.randomUUID().toString()
                        val extras =
                            FragmentNavigatorExtras(
                                binding.bottomSheetImage to "shared_view_container"
                            )
                        val action =
                            QrScannerFragmentDirections.actionNavQrToDetailsFragment(placeID = placeID)
                        findNavController().navigate(action, extras)
                        bottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
                    }
                    bottomSheet.state = BottomSheetBehavior.STATE_EXPANDED

                }
            }
        }
    }

    companion object {
        private const val TAG = "QrScannerFragment"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
