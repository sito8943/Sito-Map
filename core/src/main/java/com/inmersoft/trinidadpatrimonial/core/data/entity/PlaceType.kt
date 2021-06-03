package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "place_types")
@JsonClass(generateAdapter = true)
data class PlaceType(
    @PrimaryKey val place_type_id: Int,
    val icon: String,
    val type: String,
    val type_translation: List<TypeTranslation>,
    @Ignore
    val places_id: List<Int>
) {
    constructor(
        place_type_id: Int,
        icon: String,
        type: String,
        type_translation: List<TypeTranslation>

    ) : this(0, "", "", emptyList(), emptyList())
}