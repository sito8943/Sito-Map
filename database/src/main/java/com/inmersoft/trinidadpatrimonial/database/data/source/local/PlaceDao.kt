package com.inmersoft.trinidadpatrimonial.database.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.inmersoft.trinidadpatrimonial.database.data.entity.Place
import com.inmersoft.trinidadpatrimonial.database.data.entity.PlacesWithPlaceType
import com.inmersoft.trinidadpatrimonial.database.data.entity.PlacesWithRoutes

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Place)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(place: List<Place>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(place: Place)

    @Query("SELECT place_name FROM places")
    fun getAllPlacesName(): LiveData<List<String>>

    @Query("SELECT * FROM places")
    fun getAllPlaces(): LiveData<List<Place>>

    @Query("SELECT * FROM places WHERE place_id=:placeID ")
    suspend fun getPlaceByID(placeID: Int): Place

    @Query("DELETE FROM places ")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM places")
    suspend fun getPlacesWithPlaceType(): List<PlacesWithPlaceType>

    @Transaction
    @Query("SELECT * FROM places")
    suspend fun getPlaceWithRoutes(): List<PlacesWithRoutes>
}

