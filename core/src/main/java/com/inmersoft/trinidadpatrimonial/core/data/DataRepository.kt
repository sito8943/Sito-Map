package com.inmersoft.trinidadpatrimonial.core.data

import androidx.lifecycle.LiveData
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
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

    val allPlaces: LiveData<List<Place>> = placeDao.getAllPlaces()
    val allRoutes: LiveData<List<Route>> = routesDao.getAllRoutes()
    val allPlacesType: LiveData<List<PlaceType>> = placeTypeDao.getAllPlacesType()

}