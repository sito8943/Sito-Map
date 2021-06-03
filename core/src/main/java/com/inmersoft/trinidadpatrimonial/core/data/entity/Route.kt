package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "routes")
@JsonClass(generateAdapter = true)
data class Route(
    @PrimaryKey val route_id: Int = 0,
    val header_images: List<String>,
    val route_description: String,
    val route_name: String,
    val route_translations: List<RouteTranslation>,
    val video_promo: String,
    @Ignore
    val places_id: List<Int>
) {
    constructor(
        route_id: Int,
        header_images: List<String>,
        route_description: String,
        route_name: String,
        route_translations: List<RouteTranslation>,
        video_promo: String
    ) : this(0, emptyList(), "", "", emptyList(), "", emptyList())
}