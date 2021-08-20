package com.inmersoft.trinidadpatrimonial.database.data.entity.cross_refrences

import androidx.room.Entity

@Entity(primaryKeys = ["place_id", "route_id"])
class RoutesAndPlacesCrossRef(
    val place_id: Int,
    val route_id: Int
)