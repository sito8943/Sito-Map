package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.inmersoft.trinidadpatrimonial.core.data.converters.Converters
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "place_types")
@TypeConverters(Converters::class)
data class PlaceType(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val icon: String,
    val type: String,
    val type_translation: List<TypeTranslation>?
)