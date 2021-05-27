package com.inmersoft.trinidadpatrimoniald.qr.qrdetection

import android.annotation.SuppressLint
import android.util.Log
import android.util.Size
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.common.InputImage
import com.inmersoft.trinidadpatrimoniald.qr.camera.CameraReticleAnimator
import com.inmersoft.trinidadpatrimoniald.qr.camera.GraphicOverlay

class QrProcessor(private val graphicOverlay: GraphicOverlay) : ImageAnalysis.Analyzer {
    private val cameraReticleAnimator = CameraReticleAnimator(graphicOverlay)
    private val barcodeScanner: BarcodeScanner = QrUtils.provideBarcodeScanner()

    //TODO: Definir una interfaz para la comunicación con el fragmento
    //TODO: (Detener la cámara, Mostrar resultados, ...)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let { mediaImage ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            graphicOverlay.setCameraPreviewSize(Size(mediaImage.width, mediaImage.height))
            val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)

            barcodeScanner.process(image)
                .addOnSuccessListener { barcodes ->
                    val barcodeInCenter = barcodes.firstOrNull { barcode ->
                        val boundingBox = barcode.boundingBox ?: return@firstOrNull false
                        val box = graphicOverlay.translateRect(boundingBox)
                        val reticleBox = QrUtils.getBarcodeReticleBox(graphicOverlay)
                        reticleBox.contains(box)
                    }

                    graphicOverlay.clear()

                    when (barcodeInCenter) {
                        null -> addReticleGraphic()
                        else -> {
                            Log.d(TAG, "Barcode: ${barcodeInCenter.displayValue}")
                            addReticleGraphic()
                        }
                    }

                    graphicOverlay.invalidate()
                }
                .addOnCompleteListener { imageProxy.close() }
        }
    }

    private fun addReticleGraphic() {
        cameraReticleAnimator.start()
        graphicOverlay.add(QrReticleGraphic(graphicOverlay, cameraReticleAnimator))
    }

    companion object {
        private const val TAG = "QrProcessor"
    }
}