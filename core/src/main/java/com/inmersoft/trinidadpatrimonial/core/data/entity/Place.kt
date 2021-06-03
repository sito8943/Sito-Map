package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "places")
@JsonClass(generateAdapter = true)
data class Place(
    @PrimaryKey val place_id: Int,
    val header_images: List<String>,
    @Embedded
    val location: Location,
    val map_icon: String,
    val model3d: String,
    val pano: List<String>,
    val place_description: String,
    val place_name: String,
    val place_translations: List<PlaceTranslation>,
    val video_promo: String,
    val web: String,
    @Ignore
    val routes_id: List<Int>,
    @Ignore
    val place_type: List<Int>

) {

    constructor(
        place_id: Int,
        header_images: List<String>,
        location: Location,
        map_icon: String,
        model3d: String,
        pano: List<String>,
        place_description: String,
        place_name: String,
        place_translations: List<PlaceTranslation>,
        video_promo: String,
        web: String
    ) : this(
        0,
        emptyList(),
        Location(0.0, 0.0),
        "",
        "",
        emptyList(),
        "0.0",
        "0",
        emptyList(),
        "",
        "",
        emptyList(),
        emptyList()
    )

}