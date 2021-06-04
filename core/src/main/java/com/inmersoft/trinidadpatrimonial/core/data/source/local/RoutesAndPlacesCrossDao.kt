package com.inmersoft.trinidadpatrimonial.core.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.inmersoft.trinidadpatrimonial.core.data.entity.cross_refrences.RoutesAndPlacesCrossRef

@Dao
interface RoutesAndPlacesCrossDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addRoutesAndPlacesCrossRef(assignedCrossRef: RoutesAndPlacesCrossRef)
}