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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.transition.platform.MaterialFadeThrough
import com.google.vr.sdk.widgets.pano.VrPanoramaView
import com.inmersoft.trinidadpatrimonial.R
import kotlin.random.Random

fun ViewGroup.fadeTransitionExt(transition: android.transition.Transition = MaterialFadeThrough()) {
    TransitionManager.beginDelayedTransition(this, transition)
}

fun Context.showToastExt(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun String.smartTruncate(max: Int, suffix: String = "..."): String {
    return if (this.length < max) {
        this
    } else {
        "${this.substring(0, this.substring(0, max - suffix.length).lastIndexOf(' '))}${suffix}"
    }
}

val placeholderError =
    listOf<Int>(
        R.drawable.placeholder_1,
        R.drawable.placeholder_2,
        R.drawable.placeholder_3,
        R.drawable.placeholder_4,
        R.drawable.placeholder_5,
        R.drawable.placeholder_6,
        R.drawable.placeholder_7,
        R.drawable.placeholder_8,
        R.drawable.placeholder_9,
        R.drawable.placeholder_10,
        R.drawable.placeholder_11)

fun ImageView.loadImageCenterInsideExt(imageUri: Uri) {

    Glide.with(this)
        .load(imageUri)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(placeholderError[Random.nextInt(placeholderError.size)])
        .placeholder(placeholderError[Random.nextInt(placeholderError.size)])
        .centerInside()
        .into(this)
}

fun ImageView.loadImageCenterInsideExt(imageResource: Int) {
    Glide.with(this)
        .load(imageResource)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(placeholderError[Random.nextInt(placeholderError.size)])
        .placeholder(placeholderError[Random.nextInt(placeholderError.size)])
        .centerInside()
        .into(this)
}

fun ImageView.loadImageCenterInsideExt(imageResource: Int, imageResourcePlaceholder: Int) {
    Glide.with(this)
        .load(imageResource)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .placeholder(imageResourcePlaceholder)
        .centerInside()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.loadImageCenterCropExt(imageUri: Uri) {
    Glide.with(this)
        .load(imageUri)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .error(placeholderError[Random.nextInt(placeholderError.size)])
        .placeholder(placeholderError[Random.nextInt(placeholderError.size)])
        .centerCrop()
        .into(this)
}

fun ImageView.loadImageCenterCropExt(imageResource: Int) {
    Glide.with(this)
        .load(imageResource)
        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
        .error(placeholderError[Random.nextInt(placeholderError.size)])
        .placeholder(placeholderError[Random.nextInt(placeholderError.size)])
        .centerCrop()
        .into(this)
}

fun VrPanoramaView.loadPano360WithGlideExt(uriPanoResource: Uri, container: List<View>) {
    val vrPanoContext = this
    if (uriPanoResource.toString().isNotEmpty()) {
        Glide.with(this)
            .asBitmap()
            .load(uriPanoResource)
            .listener(object : RequestListener<Bitmap?> {

                private fun loadingDone(done: Boolean) {
                    container.forEach { currentContainer ->
                        if (done) {
                            currentContainer.visible()
                        } else {
                            currentContainer.gone()
                        }
                    }
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
        container.forEach { currentContainer ->
            currentContainer.gone()
        }
    }
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
