package com.inmersoft.trinidadpatrimonial.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.OvershootInterpolator

/**
 * SCALE and ALPHA for simple components animation
 *
 */

val showScaleX: PropertyValuesHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 0.2f, 1f)
val showScaleY: PropertyValuesHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0.2f, 1f)
val showAlpha: PropertyValuesHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)

val hideScaleX: PropertyValuesHolder = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 0.2f)
val hideScaleY: PropertyValuesHolder = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 0.2f)
val hideAlpha: PropertyValuesHolder = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)

fun showComponentWithEffect(target: Any) {
  ObjectAnimator.ofPropertyValuesHolder(target, showScaleX, showScaleY, showAlpha)
    .apply { interpolator = OvershootInterpolator() }.start()
}

fun hideComponentWithEffect(target: Any) {
  ObjectAnimator.ofPropertyValuesHolder(target, hideScaleX, hideScaleY, hideAlpha)
    .apply { interpolator = OvershootInterpolator() }.start()
}


