package com.inmersoft.trinidadpatrimonial.utils

import android.util.Log

class TrinidadQR(
    private val barcodeRaw: String,
    private val separator: String = "|",
    private val placeIdPosition: Int = 1,
) {

    companion object {
        const val TAG = "TrinidadQR"
    }

    private var _isValid = false
    private var qrDataSplitted: List<String>? = null

    init {
        if (barcodeRaw.isNotBlank()) {
            Log.d(TAG, "barcodeRaw is Not empty ")
            qrDataSplitted = getSplitted()
            if (qrDataSplitted!!.size > 1) {
                _isValid = true
            }
        }
    }

    fun isValid(): Boolean = _isValid

    fun getPlaceID(): Int {
        if (placeIdPosition < qrDataSplitted!!.size) {
            Log.d(TAG, "getPlaceID: Getting placeid by placeIdPosition")
            return qrDataSplitted!![placeIdPosition].trim().toInt()
        }
        Log.d(TAG, "getPlaceID: No intro in if conditional")
        return -1
    }

    private fun getSplitted(): List<String> {
        return barcodeRaw.split(separator)
    }
}