package com.inmersoft.trinidadpatrimonial.viewmodels

import androidx.lifecycle.ViewModel
import com.inmersoft.trinidadpatrimonial.core.data.DataRepository
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrinidadDataViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    var allPlacesName = dataRepository.allPlacesName
    var allPlaceType = dataRepository.allPlacesType
    var allPlaceTypeWithPlaces = dataRepository.allPlacesTypeWithPlaces
    var allRoutes = dataRepository.allRoutes
    var allPlaces = dataRepository.allPlaces

    suspend fun getPlaceById(placeId: Int): Place {
        return dataRepository.getPlaceById(placeId)
    }

}