package com.inmersoft.trinidadpatrimonial.home.ui.utils

import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces

object PlaceTypeFilter {
    fun filterNotEmptyPlaces(placeTypeWithPlaces: List<PlaceTypeWithPlaces>): List<PlaceTypeWithPlaces> =
        placeTypeWithPlaces.filter { placeTypeWithPlaces -> placeTypeWithPlaces.placesList.isNotEmpty() }
}