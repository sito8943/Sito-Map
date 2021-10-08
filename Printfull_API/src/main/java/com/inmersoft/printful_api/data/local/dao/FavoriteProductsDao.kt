package com.inmersoft.printful_api.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favoriteProduct: FavoriteProduct)



    @Query("DELETE FROM favorite_products WHERE productId=:productId")
    suspend fun delete(productId: Int)

    @Query("DELETE FROM favorite_products")
    suspend fun deleteAll()

    @Query("SELECT * FROM favorite_products")
    fun getAllFavoriteProducts(): Flow<List<FavoriteProduct>>

}
