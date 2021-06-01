package com.inmersoft.trinidadpatrimonial.core.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TypeTranslation(
    val lang: String,
    val type: String
)