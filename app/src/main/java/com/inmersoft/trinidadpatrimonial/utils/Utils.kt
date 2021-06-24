package com.inmersoft.trinidadpatrimonial.utils

import android.content.Context
import android.view.ViewGroup
import android.widget.Toast
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialFadeThrough

fun fadeTransition(container: ViewGroup) {
    TransitionManager.beginDelayedTransition(container, MaterialFadeThrough())
}

fun showToast(ctx: Context, message: String) {
    Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show()
}
