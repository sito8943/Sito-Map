package com.inmersoft.trinidadpatrimonial.core.data

import androidx.lifecycle.LiveData
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.core.data.entity.Route
import com.inmersoft.trinidadpatrimonial.core.data.source.local.PlaceDao
import com.inmersoft.trinidadpatrimonial.core.data.source.local.PlaceTypeDao
import com.inmersoft.trinidadpatrimonial.core.data.source.local.RoutesDao
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val placeDao: PlaceDao,
    private val routesDao: RoutesDao,
    private val placeTypeDao: PlaceTypeDao

) {

    val allPlacesName: LiveData<List<String>> = placeDao.getAllPlacesName()
    val allRoutes: LiveData<List<Route>> = routesDao.getAllRoutes()
    val allPlaces: LiveData<List<Place>> = placeDao.getAllPlaces()
    val allPlacesType: LiveData<List<PlaceType>> = placeTypeDao.getAllPlacesType()
    val allPlacesTypeWithPlaces: LiveData<List<PlaceTypeWithPlaces>> =
        placeTypeDao.getPlaceTypeWithPlaces()

    suspend fun getPlaceById(placeID: Int): Place {
        return placeDao.getPlaceByID(placeID)
    }


}