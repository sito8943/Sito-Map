package com.inmersoft.trinidadpatrimonial.core.utils

import android.content.Context
import android.util.Log
import java.io.InputStream

fun readJSONFromAsset(context: Context): String {
    val json: String
    try {
        val inputStream: InputStream = context.assets.open("trinidad-db.json")
        json = inputStream.bufferedReader().use {
            it.readText()
        }
    } catch (ex: Exception) {
        ex.localizedMessage
        Log.e("APP-Database", "readJSONFromAsset: ${ex.localizedMessage}")
        return ""
    }
    return json
}