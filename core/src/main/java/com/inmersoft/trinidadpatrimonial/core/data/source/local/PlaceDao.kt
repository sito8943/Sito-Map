package com.inmersoft.trinidadpatrimonial.core.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.inmersoft.trinidadpatrimonial.core.data.entity.Place
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlacesWithPlaceType
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlacesWithRoutes

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(place: List<Place>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(place: Place)

    @Query("SELECT * FROM places")
    fun getAllPlaces(): LiveData<List<Place>>

    @Query("DELETE FROM places ")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM places")
    suspend fun getPlacesWithPlaceType(): List<PlacesWithPlaceType>

    @Transaction
    @Query("SELECT * FROM places")
    suspend fun getPlaceWithRoutes(): List<PlacesWithRoutes>
}