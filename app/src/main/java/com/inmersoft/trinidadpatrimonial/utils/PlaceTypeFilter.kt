package com.inmersoft.trinidadpatrimonial.utils

import com.inmersoft.trinidadpatrimonial.database.data.entity.PlaceTypeWithPlaces

object PlaceTypeFilter {
    fun filterNotEmptyPlaces(placeTypeWithPlaces: List<PlaceTypeWithPlaces>): List<PlaceTypeWithPlaces> =
        placeTypeWithPlaces.filter { placeTypeWithPlaces -> placeTypeWithPlaces.placesList.isNotEmpty() }
}