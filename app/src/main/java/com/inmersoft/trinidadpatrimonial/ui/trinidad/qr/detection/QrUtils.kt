package com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.detection

import android.graphics.RectF
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.ui.trinidad.qr.camera.GraphicOverlay

object QrUtils {

    fun provideBarcodeScanner(): BarcodeScanner {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        return BarcodeScanning.getClient(options)
    }

    fun getProgressToMeetBarcodeSizeRequirement(
        overlay: GraphicOverlay,
        barcode: Barcode,
        barcodeSizeCheck: Boolean = true
    ): Float {
        return if (barcodeSizeCheck) {
            val reticleBoxWidth = getBarcodeReticleBox(overlay).width()
            val barcodeWidth = overlay.translateX(barcode.boundingBox?.width()?.toFloat() ?: 0f)
            val requiredWidth = reticleBoxWidth * 80 / 100
            (barcodeWidth / requiredWidth).coerceAtMost(1f)
        } else {
            1f
        }
    }

    fun getBarcodeReticleBox(overlay: GraphicOverlay): RectF {
        val context = overlay.context
        val overlayWidth = overlay.width.toFloat()
        val overlayHeight = overlay.height.toFloat()
        val boxSizeHalf = context.resources.getDimensionPixelOffset(R.dimen.qr_reticle_size) / 2
        val cx = overlayWidth / 2
        val cy = overlayHeight / 2
        return RectF(cx - boxSizeHalf, cy - boxSizeHalf, cx + boxSizeHalf, cy + boxSizeHalf)
    }
}