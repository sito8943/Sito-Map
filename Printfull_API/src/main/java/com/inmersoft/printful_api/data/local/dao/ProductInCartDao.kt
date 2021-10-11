package com.inmersoft.printful_api.data.local.dao

import androidx.room.*
import com.inmersoft.printful_api.data.local.model.ProductInCart
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductInCartDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(productInCart: ProductInCart)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(productInCart: ProductInCart)

    @Query("DELETE FROM product_variant_chart WHERE variantId=:variantId")
    suspend fun delete(variantId: Int)

    @Query("DELETE FROM product_variant_chart")
    suspend fun deleteAll()

    @Query("SELECT * FROM product_variant_chart")
    fun getAllProductsInChart(): Flow<List<ProductInCart>>

}
