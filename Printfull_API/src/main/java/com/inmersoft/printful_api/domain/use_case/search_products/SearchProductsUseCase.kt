package com.inmersoft.printful_api.domain.use_case.search_products

import com.inmersoft.printful_api.common.Constants
import com.inmersoft.printful_api.common.DataResult
import com.inmersoft.printful_api.common.ErrorType
import com.inmersoft.printful_api.common.ResourcesError
import com.inmersoft.printful_api.data.remote.dto.products.toProducts
import com.inmersoft.printful_api.domain.model.Products
import com.inmersoft.printful_api.domain.repository.PrintfulRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class SearchProductsUseCase(
    private val repository: PrintfulRepository
) {
    operator fun invoke(
        search: String,
        offset: Int = 0,
        limit: Int = Constants.MAX_PRODUCT_LIMIT
    ): Flow<DataResult<Products>> = flow {
        try {
            emit(DataResult.Loading())
            val products =
                repository.searchProduct(search = search, offset = offset, limit = limit)
                    .toProducts()
            emit(DataResult.Success(products))
        } catch (e: HttpException) {
            emit(DataResult.Error(e.message?.let {
                ResourcesError(
                    ErrorType.HTTP_ERROR,
                    message = it
                )
            }))
        } catch (e: IOException) {
            emit(DataResult.Error(e.message?.let {
                ResourcesError(
                    ErrorType.IO_ERROR,
                    message = it
                )
            }))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message?.let {
                ResourcesError(
                    ErrorType.UNKNOW_ERROR,
                    message = it
                )
            }))
        }
    }

}