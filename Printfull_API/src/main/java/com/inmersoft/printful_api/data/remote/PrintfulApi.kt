package com.inmersoft.printful_api.data.remote

import com.inmersoft.printful_api.common.Constants
import com.inmersoft.printful_api.data.remote.dto.details.DetailsDTO
import com.inmersoft.printful_api.data.remote.dto.products.ProductsDTO
import com.inmersoft.printful_api.data.remote.dto.store_info.StoreInfoDTO
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrintfulApi {

    companion object {
        const val API_BASE_URL = "https://api.printful.com/"
    }

    @GET("/store/products")
    suspend fun getProducts(
        @Query("offset") offset: Int = 0,
        @Query("limit") limit: Int = Constants.MAX_PRODUCT_LIMIT,
    ): ProductsDTO

    @GET("/store/products/{productID}")
    suspend fun getDetailsById(
        @Path("productID") productID: String
    ): DetailsDTO


    @GET("store/products")
    suspend fun searchProducts(
        @Query("offset") offset: Int = 0,
        @Query("search") search: String,
        @Query("limit") limit: Int = Constants.MAX_PRODUCT_LIMIT,
    ): ProductsDTO

    @GET("store/")
    suspend fun getStoreInfo(
    ): StoreInfoDTO

}