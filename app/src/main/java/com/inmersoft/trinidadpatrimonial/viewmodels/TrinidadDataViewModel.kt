package com.inmersoft.trinidadpatrimonial.viewmodels

import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.inmersoft.trinidadpatrimonial.core.data.DataRepository
import com.inmersoft.trinidadpatrimonial.utils.PlaceTypeFilter
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
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

}