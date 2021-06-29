package com.inmersoft.trinidadpatrimonial.qr.fragments

import android.Manifest
import android.content.pm.PackageManager
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
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.common.util.concurrent.ListenableFuture
import com.inmersoft.trinidadpatrimonial.databinding.QrScannerFragmentBinding
import com.inmersoft.trinidadpatrimonial.map.ui.MapFragmentDirections
import com.inmersoft.trinidadpatrimonial.qr.qrdetection.QrProcessor
import com.inmersoft.trinidadpatrimonial.utils.TrinidadAssets
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.SheetData
import com.inmersoft.trinidadpatrimonial.utils.trinidadsheet.TrinidadBottomSheet
import com.inmersoft.trinidadpatrimonial.viewmodels.TrinidadDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val trinidadDataViewModel: TrinidadDataViewModel by activityViewModels()

    private lateinit var trinidadBottomSheet: TrinidadBottomSheet

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

        trinidadBottomSheet = TrinidadBottomSheet(requireContext(), binding.root as ViewGroup)

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
                withContext(Dispatchers.Main) {
                    val uriImage = Uri.parse(
                        TrinidadAssets.getAssetFullPath(
                            place.header_images[0],
                            TrinidadAssets.FILE_JPG_EXTENSION
                        )
                    )
                    val webURI = Uri.parse(place.web)
                    val data =
                        SheetData(
                            place.place_id,
                            uriImage,
                            place.place_name,
                            place.place_description,
                            webURI
                        )
                    trinidadBottomSheet.bindData(data)
                    trinidadBottomSheet.navigateTo(
                        MapFragmentDirections.actionNavMapToDetailsFragment(
                            placeID
                        ), findNavController()
                    )
                    trinidadBottomSheet.show()
                }
            }
        }
    }

    companion object {
        private const val TAG = "QrScannerFragment"
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}
