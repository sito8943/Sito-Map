package com.inmersoft.trinidadpatrimonial.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.inmersoft.trinidadpatrimonial.R


fun ViewGroup.fadeTransitionExt() {
    TransitionManager.beginDelayedTransition(this, MaterialFadeThrough())
}

fun Context.showToastExt(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun ImageView.loadImageWithGlideExt(imageUri: Uri) {
    Glide.with(this)
        .load(imageUri)
        .centerCrop()
        .error(R.drawable.placeholder_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.placeholder_error)
        .into(this)
}

fun ImageView.loadImageWithGlideExt(imageResource: Int) {
    Glide.with(this)
        .load(this.resources.getDrawable(imageResource, this.context.theme))
        .centerCrop()
        .error(R.drawable.placeholder_error)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .placeholder(R.drawable.placeholder_error)
        .into(this)
}

fun VrPanoramaView.loadPano360WithGlideExt(uriPanoResource: Uri, container: View? = null) {
    val vrPanoContext = this
    if (uriPanoResource.toString().isNotEmpty()) {
        Glide.with(this)
            .asBitmap()
            .placeholder(R.drawable.placeholder_error)
            .load(uriPanoResource)
            .listener(object : RequestListener<Bitmap?> {

                private fun loadingDone(done: Boolean) {
                    container?.visibility = if (done) View.VISIBLE else View.GONE
                }

                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    isFirstResource: Boolean,
                ): Boolean {
                    loadingDone(false)
                    return false
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean,
                ): Boolean {
                    loadingDone(true)
                    return false
                }
            })
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?,
                ) {
                    val options = VrPanoramaView.Options()
                    options.inputType = VrPanoramaView.Options.TYPE_MONO
                    vrPanoContext.setInfoButtonEnabled(false)
                    vrPanoContext.setFullscreenButtonEnabled(true)
                    vrPanoContext.setStereoModeButtonEnabled(true)
                    vrPanoContext.setTouchTrackingEnabled(true)
                    vrPanoContext.loadImageFromBitmap(resource, options)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Add other icon
                }
            })
    } else {
        container?.visibility = View.GONE
    }
}