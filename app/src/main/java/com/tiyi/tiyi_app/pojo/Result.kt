package com.tiyi.tiyi_app.pojo

import retrofit2.HttpException
import java.net.ConnectException

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    // Code 400
    data class BadRequest(val exception: HttpException) : Result<Nothing>()
    // Code 401
    data class Unauthorized(val exception: HttpException) : Result<Nothing>()
    // Code 500
    data class ServerInternalError(val exception: HttpException) : Result<Nothing>()
    // Not connected
    data class NetworkError(val exception: ConnectException) : Result<Nothing>()
}

// Not as promised in the API document
class CorruptedApiException : RuntimeException()