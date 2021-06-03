package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity(tableName = "place_types")
@JsonClass(generateAdapter = true)
data class PlaceType(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val icon: String,
    val type: String,
    val type_translation: List<TypeTranslation>
)