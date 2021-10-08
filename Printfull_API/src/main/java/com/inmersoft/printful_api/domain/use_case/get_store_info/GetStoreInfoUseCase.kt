package com.inmersoft.printful_api.domain.use_case.get_store_info

import com.inmersoft.printful_api.common.DataResult
import com.inmersoft.printful_api.common.ErrorType
import com.inmersoft.printful_api.common.ResourcesError
import com.inmersoft.printful_api.data.remote.dto.store_info.toStoreInfo
import com.inmersoft.printful_api.domain.model.StoreInfo
import com.inmersoft.printful_api.domain.repository.PrintfulRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException

class GetStoreInfoUseCase(
    private val repository: PrintfulRepository
) {
    operator fun invoke(
    ): Flow<DataResult<StoreInfo>> = flow {
        try {
            emit(DataResult.Loading())
            val products =
                repository.getStoreInfo().toStoreInfo()
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