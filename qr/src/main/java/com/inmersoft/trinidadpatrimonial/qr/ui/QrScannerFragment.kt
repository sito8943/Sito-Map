package com.inmersoft.trinidadpatrimonial.qr.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
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
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.inmersoft.trinidadpatrimonial.qr.databinding.QrScannerFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class QrScannerFragment : Fragment() {
    private lateinit var cameraPreview: PreviewView
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private var camera: Camera? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = QrScannerFragmentBinding.inflate(inflater, container, false)
        cameraPreview = binding.cameraView
        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        if (isPermissionGranted())
            startCamera()
        else
            requestPermission.launch(REQUIRED_PERMISSION)

        cameraExecutor = Executors.newSingleThreadExecutor()

        return binding.root
    }

    private fun startCamera() {
        val fragmentContext = requireContext()
        cameraProviderFuture = ProcessCameraProvider.getInstance(fragmentContext)
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

        imageAnalysis.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { image ->
            val rotationDegrees = image.imageInfo.rotationDegrees
            // insert your code here.
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        })

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

    companion object {
        private const val TAG = "QrScannerFragment"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}