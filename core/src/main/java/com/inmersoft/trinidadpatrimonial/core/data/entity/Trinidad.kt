package com.inmersoft.trinidadpatrimonial.core.data.entity

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Trinidad(
    val place_type: List<PlaceType>,
    val places: List<Place>,
    val routes: List<Route>
)