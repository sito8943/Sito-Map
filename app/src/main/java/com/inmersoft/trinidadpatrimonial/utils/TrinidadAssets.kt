package com.inmersoft.trinidadpatrimonial.utils

object TrinidadAssets {

    private const val ASSETS_FOLDER = "file:///android_asset"
    const val jpg = "jpg"
    const val FILE_PNG_EXTENSION = "png"
    const val FILE_WEBP_EXTENSION = "webp"
    const val FILE_SVG_EXTENSION = "svg"

    fun getAssetFullPath(url: String, fileExtension: String): String {
        return "$ASSETS_FOLDER/$url.$fileExtension"
    }
}