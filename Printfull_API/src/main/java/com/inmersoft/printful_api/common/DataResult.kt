package com.inmersoft.printful_api.common

sealed class DataResult<T>(val data: T? = null, val error: ResourcesError? = null) {
    class Success<T>(data: T) : DataResult<T>(data)
    class Error<T>(error: ResourcesError? = null, data: T? = null) :
        DataResult<T>(data = data, error = error)

    class Loading<T>(data: T? = null) : DataResult<T>(data)
}

data class ResourcesError(val errorType: ErrorType, val message: String)

enum class ErrorType {
    IO_ERROR,
    HTTP_ERROR,
    UNKNOW_ERROR
}