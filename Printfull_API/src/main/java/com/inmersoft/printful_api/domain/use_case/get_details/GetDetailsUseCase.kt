package com.inmersoft.printful_api.domain.use_case.get_details

import com.inmersoft.printful_api.common.DataResult
import com.inmersoft.printful_api.common.ErrorType.*
import com.inmersoft.printful_api.common.ResourcesError
import com.inmersoft.printful_api.data.remote.dto.details.toDetails
import com.inmersoft.printful_api.domain.model.Details
import com.inmersoft.printful_api.domain.repository.PrintfulRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetDetailsUseCase(
    private val repository: PrintfulRepository
) {
    operator fun invoke(
        productID: String
    ): Flow<DataResult<Details>> = flow {
        try {
            emit(DataResult.Loading())
            val products =
                repository.getDetailsById(productID = productID).toDetails()
            emit(DataResult.Success(products))
        } catch (e: HttpException) {
            emit(DataResult.Error(e.message?.let { ResourcesError(HTTP_ERROR, message = it) }))
        } catch (e: IOException) {
            emit(DataResult.Error(e.message?.let { ResourcesError(IO_ERROR, message = it) }))
        } catch (e: Exception) {
            emit(DataResult.Error(e.message?.let { ResourcesError(UNKNOW_ERROR, message = it) }))
        }
    }

}