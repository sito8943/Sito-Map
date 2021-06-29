package com.inmersoft.trinidadpatrimonial.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.inmersoft.trinidadpatrimonial.core.data.DataRepository
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.utils.PlaceTypeFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class TrinidadDataViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    var allPlacesName = dataRepository.allPlacesName
    var allPlaceType = dataRepository.allPlacesType
    var allPlaceTypeWithPlaces = dataRepository.allPlacesTypeWithPlaces.switchMap {
        liveData {
            emit(
                PlaceTypeFilter.filterNotEmptyPlaces(it)
            )
        }
    }
    var allRoutes = dataRepository.allRoutes
    var allPlaces = dataRepository.allPlaces

    suspend fun getPlaceById(placeId: Int): Place {
        return dataRepository.getPlaceById(placeId)
    }

}