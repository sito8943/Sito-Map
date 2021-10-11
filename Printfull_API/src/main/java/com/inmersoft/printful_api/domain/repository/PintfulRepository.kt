package com.inmersoft.printful_api.domain.repository

import com.inmersoft.printful_api.common.Constants
import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.data.remote.dto.details.DetailsDTO
import com.inmersoft.printful_api.data.remote.dto.products.ProductsDTO
import com.inmersoft.printful_api.data.remote.dto.store_info.StoreInfoDTO
import kotlinx.coroutines.flow.Flow

interface PrintfulRepository {

    suspend fun getProducts(
        limit: Int = Constants.MAX_PRODUCT_LIMIT,
        offset: Int = 0
    ): ProductsDTO

    suspend fun getDetailsById(productID: String): DetailsDTO

    suspend fun getStoreInfo(): StoreInfoDTO

    suspend fun searchProduct(limit: Int, offset: Int, search: String): ProductsDTO


    //Chart in the Database

    suspend fun insertVariant(productInCart: ProductInCart)

    suspend fun updateVariant(productInCart: ProductInCart)

    suspend fun deleteVariantById(roomSyncVariantID: Int)

    suspend fun deleteAllDataInChart()

    fun getAllProductsInChart(): Flow<List<ProductInCart>>

    //Add and delete favorite products

    suspend fun insertFavorite(productFavorite: FavoriteProduct)
    fun getAllFavorites(): Flow<List<FavoriteProduct>>
    suspend fun deleteFavorite(productFavoriteId: Int)
    suspend fun deleteAllFavorite()

}
