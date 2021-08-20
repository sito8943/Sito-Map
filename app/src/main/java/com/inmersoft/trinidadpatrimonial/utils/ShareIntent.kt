package com.inmersoft.trinidadpatrimonial.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.inmersoft.trinidadpatrimonial.R

object ShareIntent {
    fun shareIt(ctx: Context, bm: Bitmap, shareText: String, shareBy: String) {
        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        intent.putExtra(
            Intent.EXTRA_TEXT,
            shareText
        )
        val path = MediaStore.Images.Media.insertImage(ctx.contentResolver, bm, shareText, shareBy)
        val screenshotUri: Uri = Uri.parse(path)
        intent.putExtra(Intent.EXTRA_STREAM, screenshotUri)
        intent.type = "image/*"
        ctx.startActivity(
            Intent.createChooser(
                intent,
                shareBy
            )
        )
    }

    fun loadImageAndShare(ctx: Context, imageURI: Uri, shareText: String, shareBy: String) {
        Glide.with(ctx)
            .asBitmap()
            .load(imageURI)
            .placeholder(R.drawable.placeholder_error)
            .error(R.drawable.placeholder_error)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    shareIt(
                        ctx,
                        resource,
                        shareText,
                        shareBy
                    )
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
    }
}