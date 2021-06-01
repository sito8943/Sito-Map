package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "routes")
data class Route(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val header_images: List<String>,
    val route_description: String,
    val route_name: String,
    val route_translations: List<RouteTranslation>,
    val video_promo: String
)