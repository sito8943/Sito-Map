package com.inmersoft.trinidadpatrimonial.viewmodels

import android.util.Log
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.inmersoft.trinidadpatrimonial.database.data.DataRepository
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.database.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.preferences.UserPreferences
import com.inmersoft.trinidadpatrimonial.preferences.UserPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Flow
import javax.inject.Inject

@HiltViewModel
class TrinidadDataViewModel @Inject constructor(
    private val dataRepository: DataRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
) :
    ViewModel() {

    val userPreferences = userPreferencesRepository.getPrefs()

    fun onSetUserPreferences(userPreferences: UserPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.setUserPreferences(userPreferences)
        }
    }

    val allPlacesName = dataRepository.allPlacesName

    private val _allPlaceTypeWithPlaces = MutableLiveData<List<PlaceTypeWithPlaces>>()

    val allPlaceTypeWithPlaces: LiveData<List<PlaceTypeWithPlaces>> = _allPlaceTypeWithPlaces

    var allPlaces = dataRepository.allPlaces

    var allRoutes = dataRepository.allRoutes

    private val _currentPlaceToBottomSheet = MutableLiveData<Place?>()

    val currentPlaceToBottomSheet: LiveData<Place?> get() = _currentPlaceToBottomSheet

    private var parent = "TrinidadDataViewModel"

    private val _showProgressLoading = MutableLiveData<Boolean>()

    val showProgressLoading: LiveData<Boolean> = _showProgressLoading

    private val _placeTypeFiltered = MutableLiveData<PlaceTypeWithPlaces>()

    val placeTypeFiltered: LiveData<PlaceTypeWithPlaces> = _placeTypeFiltered

    fun onMapFilter(id: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            val result = withContext(Dispatchers.IO) {
                val placesFiltred = dataRepository.getPlaceTypeById(id)
                placesFiltred
            }
            _placeTypeFiltered.value = result
        }
    }

    init {
        _showProgressLoading.value = true
    }

    //TODO (Make the pagination when scroll down)
    fun onLoadMainRecycleViewData() {
        viewModelScope.launch(Dispatchers.Main) {
            _allPlaceTypeWithPlaces.value =
                withContext(Dispatchers.IO) {
                    delay(300)
                    dataRepository.allPlacesTypeWithPlaces().filter { it.placesList.isNotEmpty() }
                }!!
            _showProgressLoading.value = false
        }
    }

    fun onBottomSheetSetInfo(placeId: Int, _parent: String) {
        viewModelScope.launch {
            val place = withContext(Dispatchers.IO) { dataRepository.getPlaceById(placeId) }
            parent = _parent
            _currentPlaceToBottomSheet.value = place
        }
    }

    fun onMapDestroy() {
        _currentPlaceToBottomSheet.value = null
    }

    fun isParent(_parent: String): Boolean {
        Log.d(TAG, "isParent: _parent: $_parent parent:$parent")
        return _parent == parent
    }

    companion object {
        const val TAG = "TrinidadDataViewModel"
    }
}