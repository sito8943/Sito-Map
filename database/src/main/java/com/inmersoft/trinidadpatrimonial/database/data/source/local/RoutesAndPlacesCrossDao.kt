package com.inmersoft.trinidadpatrimonial.database.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.inmersoft.trinidadpatrimonial.database.data.entity.cross_refrences.RoutesAndPlacesCrossRef

@Dao
interface RoutesAndPlacesCrossDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRoutesAndPlacesCrossRef(assignedCrossRef: RoutesAndPlacesCrossRef)
}