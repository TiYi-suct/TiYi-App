package com.tiyi.tiyi_app.pojo

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    // Code 400
    data class BadRequest(val exception: Exception) : Result<Nothing>()
    // Code 401
    data class Unauthorized(val exception: Exception) : Result<Nothing>()
    // Code 500
    data class ServerInternalError(val exception: Exception) : Result<Nothing>()
    // Not connected
    data class NetworkError(val exception: Exception) : Result<Nothing>()
}