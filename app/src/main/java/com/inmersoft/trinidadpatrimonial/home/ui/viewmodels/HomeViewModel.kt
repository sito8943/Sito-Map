package com.inmersoft.trinidadpatrimonial.home.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inmersoft.trinidadpatrimonial.core.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeFragmentViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }

    var allPlaces = dataRepository.allPlaces
    var allPlaceType = dataRepository.allPlacesType
    var allPlaceTypeWithRoutes = dataRepository.allPlacesTypeWithRoutes
    var allRoutes = dataRepository.allRoutes

    val text: LiveData<String> = _text
}