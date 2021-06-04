package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.inmersoft.trinidadpatrimonial.core.data.entity.cross_refrences.RoutesAndPlacesCrossRef

data class RoutesWithPlaces(
    @Embedded val route: Route,
    @Relation(
        parentColumn = "route_id",
        entityColumn = "place_id",
        associateBy = Junction(RoutesAndPlacesCrossRef::class)
    )
    val placesList: List<Place> = emptyList()
)