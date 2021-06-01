package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "places")
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val header_images: List<String>,
    val location: Location,
    val map_icon: String,
    val model3d: String,
    val pano: List<String>,
    val place_description: String,
    val place_name: String,
    val place_translations: List<PlaceTranslation>,
    val place_type: List<Int>,
    val video_promo: String,
    val web: String
)