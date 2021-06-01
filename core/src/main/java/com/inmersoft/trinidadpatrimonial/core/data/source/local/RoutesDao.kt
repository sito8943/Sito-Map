package com.inmersoft.trinidadpatrimonial.core.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.inmersoft.trinidadpatrimonial.core.data.entity.Route

@Dao
interface RoutesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: Route)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(route: Route)

    @Query("SELECT * FROM routes")
    fun getAllPlaces(): LiveData<List<Route>>

    @Query("DELETE FROM routes")
    suspend fun deleteAll()
}