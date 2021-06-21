package com.inmersoft.trinidadpatrimonial.utils

object TrinidadAssets {

    fun getAssetFullPath(url: String): String {
        return "$ASSETS_FOLDER/$url.$FILE_EXTENSION"
    }
}