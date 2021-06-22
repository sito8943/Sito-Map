package com.inmersoft.trinidadpatrimonial.utils

import android.view.ViewGroup
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFadeThrough

fun fadeTransition(container: ViewGroup) {
    TransitionManager.beginDelayedTransition(container, MaterialFadeThrough())
}
