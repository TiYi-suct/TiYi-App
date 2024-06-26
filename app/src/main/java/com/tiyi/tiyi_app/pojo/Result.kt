package com.tiyi.tiyi_app.pojo

import com.google.gson.Gson
import com.tiyi.tiyi_app.dto.CommonResponseModel
import retrofit2.HttpException

private fun parseExceptionMsg(exception: HttpException): String {
    return Gson().fromJson(
        exception.response()?.errorBody()?.string(),
        CommonResponseModel::class.java
    ).msg
}

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>() {
        override val message: String
            get() = "Success"
    }

    // Code 400
    data class BadRequest(val exception: HttpException) : Result<Nothing>() {
        override val message: String
            get() = parseExceptionMsg(exception)
    }

    // Code 401
    data class Unauthorized(val exception: HttpException) : Result<Nothing>() {
        override val message: String
            get() = parseExceptionMsg(exception)
    }

    // Code 500
    data class ServerInternalError(val exception: HttpException) : Result<Nothing>() {
        override val message = "远程服务器内部错误"
    }

    // Not connected
    data class NetworkError(val exception: Exception) : Result<Nothing>() {
        override val message = "网络连接失败"
    }

    abstract val message: String
}

// Not as promised in the API document
class CorruptedApiException(
    message: String = "Impossible Api response"
) : RuntimeException(message)