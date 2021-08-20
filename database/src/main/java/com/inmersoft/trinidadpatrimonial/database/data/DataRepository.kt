package com.inmersoft.trinidadpatrimonial.database.data

import androidx.lifecycle.LiveData
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.database.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.database.data.entity.PlaceTypeWithPlaces
import com.inmersoft.trinidadpatrimonial.database.data.entity.Route
import com.inmersoft.trinidadpatrimonial.database.data.source.local.PlaceDao
import com.inmersoft.trinidadpatrimonial.database.data.source.local.PlaceTypeDao
import com.inmersoft.trinidadpatrimonial.database.data.source.local.RoutesDao
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val placeDao: PlaceDao,
    private val routesDao: RoutesDao,
    private val placeTypeDao: PlaceTypeDao

) {

    val allPlacesName: LiveData<List<String>> = placeDao.getAllPlacesName()
    val allRoutes: LiveData<List<Route>> = routesDao.getAllRoutes()
    val allPlaces: LiveData<List<Place>> = placeDao.getAllPlaces()
    val placesTypeLiveData: LiveData<List<PlaceType>> = placeTypeDao.getAllPlacesType()

    suspend fun allPlacesTypeWithPlaces() = placeTypeDao.getPlaceTypeWithPlaces()
    suspend fun getPlaceById(placeID: Int): Place {
        return placeDao.getPlaceByID(placeID)
    }

    suspend fun getPlaceTypeById(id: Int): PlaceTypeWithPlaces {
       return placeTypeDao.getPlacesTypeById(id)
    }


}