package com.inmersoft.trinidadpatrimonial.core.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceType
import com.inmersoft.trinidadpatrimonial.core.data.entity.PlaceTypeWithPlaces

@Dao
interface PlaceTypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(placeType: PlaceType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(place: List<PlaceType>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(placeType: PlaceType)

    @Query("SELECT * FROM place_types")
    fun getAllPlacesType(): LiveData<List<PlaceType>>

    @Query("DELETE FROM place_types")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * FROM place_types")
    fun getPlaceTypeWithPlaces(): LiveData<List<PlaceTypeWithPlaces>>

}