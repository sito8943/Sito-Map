package com.inmersoft.trinidadpatrimonial.core.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlaceTranslation(
    val lang: String,
    val place_description: String,
    val place_name: String,
    val video_promo: String
)