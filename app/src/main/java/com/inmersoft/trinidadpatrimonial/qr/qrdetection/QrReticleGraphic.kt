package com.inmersoft.trinidadpatrimonial.qr.qrdetection

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.core.content.ContextCompat
import com.inmersoft.trinidadpatrimonial.R
import com.inmersoft.trinidadpatrimonial.qr.camera.CameraReticleAnimator
import com.inmersoft.trinidadpatrimonial.qr.camera.GraphicOverlay

internal class QrReticleGraphic(
    overlay: GraphicOverlay,
    private val animator: CameraReticleAnimator
) : QrGraphicBase(overlay) {
    private val ripplePaint: Paint
    private val rippleSizeOffset: Int
    private val rippleStrokeWidth: Int
    private val rippleAlpha: Int

    init {
        val resources = overlay.resources
        ripplePaint = Paint()
        ripplePaint.style = Paint.Style.STROKE
        ripplePaint.color = ContextCompat.getColor(context, R.color.reticle_ripple)
        rippleSizeOffset = resources.getDimensionPixelOffset(R.dimen.qr_reticle_ripple_size_offset)
        rippleStrokeWidth =resources.getDimensionPixelOffset(R.dimen.qr_reticle_ripple_stroke_width)
        rippleAlpha = ripplePaint.alpha
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        // Draws the ripple to simulate the breathing animation effect.
        ripplePaint.alpha = (rippleAlpha * animator.rippleAlphaScale).toInt()
        ripplePaint.strokeWidth = rippleStrokeWidth * animator.rippleStrokeWidthScale
        val offset = rippleSizeOffset * animator.rippleSizeScale
        val rippleRect = RectF(
            boxRect.left - offset,
            boxRect.top - offset,
            boxRect.right + offset,
            boxRect.bottom + offset
        )
        canvas.drawRoundRect(rippleRect, boxCornerRadius, boxCornerRadius, ripplePaint)
    }
}