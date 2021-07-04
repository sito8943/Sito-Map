package com.inmersoft.trinidadpatrimonial.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inmersoft.trinidadpatrimonial.core.data.DataRepository
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TrinidadDataViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    var allPlacesName = dataRepository.allPlacesName

    private val _allPlaceTypeWithPlaces = MutableLiveData<List<PlaceTypeWithPlaces>>()
    val allPlaceTypeWithPlaces: LiveData<List<PlaceTypeWithPlaces>> = _allPlaceTypeWithPlaces

    var allPlaces = dataRepository.allPlaces

    private val _currentPlaceToBottomSheet = MutableLiveData<Place?>()
    val currentPlaceToBottomSheet: LiveData<Place?> get() = _currentPlaceToBottomSheet

    private var parent = "TrinidadDataViewModel"

    private var loadedMainData = false
    private val _showProgressLoading = MutableLiveData<Boolean>()
    val showProgressLoading: LiveData<Boolean> = _showProgressLoading


    //TODO (Make the pagination when scroll down)

    fun onLoadMainRecycleViewData() {
        viewModelScope.launch(Dispatchers.Main) {
            _showProgressLoading.value = true
            _allPlaceTypeWithPlaces.value =
                withContext(Dispatchers.IO) { dataRepository.allPlacesTypeWithPlaces() }!!
            _showProgressLoading.value = false
            loadedMainData = true
        }
    }

    fun onBottomSheetSetInfo(placeId: Int, _parent: String) {
        viewModelScope.launch {
            val place = withContext(Dispatchers.IO) { dataRepository.getPlaceById(placeId) }
            _currentPlaceToBottomSheet.value = place
            parent = _parent
        }
    }

    fun onMapDestroy() {
        _currentPlaceToBottomSheet.value = null
    }

    fun isParent(_parent: String): Boolean {
        return _parent == parent
    }

}