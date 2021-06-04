package com.inmersoft.trinidadpatrimonial.core.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.inmersoft.trinidadpatrimonial.core.data.entity.cross_refrences.PlaceTypesAndPlacesCrossRef

@Dao
interface PlaceTypesAndPlacesCrossDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addPlaceAndPlaceTypeCrossRef(assignedCrossRef: PlaceTypesAndPlacesCrossRef)
}