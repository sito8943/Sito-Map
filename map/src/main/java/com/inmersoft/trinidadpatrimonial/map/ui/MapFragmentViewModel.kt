package com.inmersoft.trinidadpatrimonial.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.data.entity.TypeTranslation

class MapFragmentViewModel : ViewModel() {

    //TODO ( Los datos de allPlaceType se solicitaran al DAO )
    private val _allPlaceType = MutableLiveData<List<PlaceType>>().apply {
        value = getAllPlaceType()
    }

    val allPlaceType: LiveData<List<PlaceType>> = _allPlaceType

    fun getAllPlaceType(): List<PlaceType> {
        return listOf(
            PlaceType(
                0,
                "Hotel",
                "url_Image",
                listOf<TypeTranslation>(TypeTranslation("es", "hbwefi"))
            ),
            PlaceType(
                0,
                "Hotel",
                "url_Image",
                listOf<TypeTranslation>(TypeTranslation("es", "hbwefi"))
            ),
            PlaceType(
                0,
                "Hotel",
                "url_Image",
                listOf<TypeTranslation>(TypeTranslation("es", "hbwefi"))
            ),
            PlaceType(
                0,
                "Hotel",
                "url_Image",
                listOf<TypeTranslation>(TypeTranslation("es", "hbwefi"))
            ),
            PlaceType(
                0,
                "Hotel",
                "url_Image",
                listOf<TypeTranslation>(TypeTranslation("es", "hbwefi"))
            ),
        )
    }

}