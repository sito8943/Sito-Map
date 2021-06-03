package com.inmersoft.trinidadpatrimonial.core.data.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.inmersoft.trinidadpatrimonial.core.data.entity.cross_refrences.PlaceTypesAndPlacesCrossRef

data class PlacesWithPlaceType(
    @Embedded val place: Place,
    @Relation(
        parentColumn = "place_id",
        entityColumn = "place_type_id",
        associateBy = Junction(PlaceTypesAndPlacesCrossRef::class)
    )
    val placesTypeList: List<PlaceType>
)