package com.inmersoft.trinidadpatrimonial.utils.trinidadsheet

import android.net.Uri

data class SheetData(
    val id: Int,
    val imageURI: Uri,
    val headerTitle: String,
    val miniDescription: String,
    val webUrl: Uri
) {
    fun isSame(_id: Int): Boolean {
        if (id == _id) {
            return true
        }
        return false
    }
}
