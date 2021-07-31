package com.inmersoft.trinidadpatrimonial.database.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.inmersoft.trinidadpatrimonial.database.data.entity.cross_refrences.PlaceTypesAndPlacesCrossRef

data class PlaceTypeWithPlaces(
    @Embedded val placeType: PlaceType,
    @Relation(
        parentColumn = "place_type_id",
        entityColumn = "place_id",
        associateBy = Junction(PlaceTypesAndPlacesCrossRef::class)
    )
    val placesList: List<Place> = emptyList()
)



