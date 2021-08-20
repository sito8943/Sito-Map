package com.inmersoft.trinidadpatrimonial.database.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RouteTranslation(
    val lang: String,
    val route_description: String,
    val route_name: String,
    val video_promo: String
)