package com.inmersoft.printful_api.data.repository

import com.inmersoft.printful_api.data.local.dao.FavoriteProductsDao
import com.inmersoft.printful_api.data.local.dao.ProductInCartDao
import com.inmersoft.printful_api.data.local.model.FavoriteProduct
import com.inmersoft.printful_api.data.local.model.ProductInCart
import com.inmersoft.printful_api.data.remote.PrintfulApi
import com.inmersoft.printful_api.data.remote.dto.details.DetailsDTO
import com.inmersoft.printful_api.data.remote.dto.products.ProductsDTO
import com.inmersoft.printful_api.data.remote.dto.store_info.StoreInfoDTO
import com.inmersoft.printful_api.domain.repository.PrintfulRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PrintfulRepositoryImpl @Inject constructor(
    private val apiPrintfulApi: PrintfulApi,
    private val productInCartDao: ProductInCartDao,
    private val favoriteProductsDao: FavoriteProductsDao,


    ) : PrintfulRepository {
    override suspend fun getProducts(limit: Int, offset: Int): ProductsDTO {
        return apiPrintfulApi.getProducts(offset = offset, limit = limit)
    }

    override suspend fun getDetailsById(productID: String): DetailsDTO {
        return apiPrintfulApi.getDetailsById(productID = productID)
    }

    override suspend fun getStoreInfo(): StoreInfoDTO {
        return apiPrintfulApi.getStoreInfo()
    }

    override suspend fun searchProduct(limit: Int, offset: Int, search: String): ProductsDTO {
        return apiPrintfulApi.searchProducts(offset = offset, limit = limit, search = search)
    }

    override suspend fun insertVariant(productInCart: ProductInCart) {
        productInCartDao.insert(productInCart = productInCart)
    }

    override suspend fun updateVariant(productInCart: ProductInCart) {
        productInCartDao.update(productInCart = productInCart)
    }

    override suspend fun deleteVariantById(roomSyncVariantID: Int) {
        productInCartDao.delete(variantId = roomSyncVariantID)
    }

    override suspend fun deleteAllDataInChart() {
        productInCartDao.deleteAll()
    }

    override fun getAllProductsInChart(): Flow<List<ProductInCart>> {
        return productInCartDao.getAllProductsInChart()
    }

    override suspend fun insertFavorite(productFavorite: FavoriteProduct) {
        favoriteProductsDao.insert(productFavorite)
    }

    override fun getAllFavorites(): Flow<List<FavoriteProduct>> {
        return favoriteProductsDao.getAllFavoriteProducts()
    }

    override suspend fun deleteFavorite(productFavoriteId: Int) {
        favoriteProductsDao.delete(productFavoriteId)
    }

    override suspend fun deleteAllFavorite() {
        favoriteProductsDao.deleteAll()
    }

}