package com.inmersoft.trinidadpatrimonial.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType

class MapFragmentViewModel : ViewModel() {

    //TODO ( Los datos de allPlaceType se solicitaran al DAO )
    private val _allPlaceType = MutableLiveData<List<PlaceType>>().apply {
        value = getAllPlaceType()
    }

    val allPlaceType: LiveData<List<PlaceType>> = _allPlaceType

    fun getAllPlaceType(): List<PlaceType> {
        return listOf(
            PlaceType("Hotel", "url_Image"),
            PlaceType("CasaColonial Santa Ana", "url_Image"),
            PlaceType("Restaurant", "url_Image"),
            PlaceType("Playas", "url_Image"),
            PlaceType("Playas", "url_Image"),
        )
    }

}