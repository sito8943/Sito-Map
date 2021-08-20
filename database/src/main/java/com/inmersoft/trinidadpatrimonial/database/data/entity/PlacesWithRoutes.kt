package com.inmersoft.trinidadpatrimonial.database.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.inmersoft.trinidadpatrimonial.database.data.entity.cross_refrences.RoutesAndPlacesCrossRef

data class PlacesWithRoutes(
    @Embedded val place: Place,
    @Relation(
        parentColumn = "place_id",
        entityColumn = "route_id",
        associateBy = Junction(
            value = RoutesAndPlacesCrossRef::class
        )
    )
    val routesList: List<Route>
)