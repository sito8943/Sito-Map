package com.inmersoft.trinidadpatrimonial.database.data.entity.cross_refrences

import androidx.room.Entity

@Entity(primaryKeys = ["place_id", "place_type_id"])
class PlaceTypesAndPlacesCrossRef(
    val place_id: Int,
    val place_type_id: Int
)