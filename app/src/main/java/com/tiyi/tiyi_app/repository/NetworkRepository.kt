package com.tiyi.tiyi_app.repository

import com.tiyi.tiyi_app.api.MusicApiService
import com.tiyi.tiyi_app.application.TokenManager
import com.tiyi.tiyi_app.dto.AnalysisItemsModel
import com.tiyi.tiyi_app.dto.AudioDetailsModel
import com.tiyi.tiyi_app.dto.AudioListModel
import com.tiyi.tiyi_app.dto.AudioUploadResponseModel
import com.tiyi.tiyi_app.dto.CommonResponseModel
import com.tiyi.tiyi_app.dto.ConsumptionCheckModel
import com.tiyi.tiyi_app.dto.FileResponseModel
import com.tiyi.tiyi_app.dto.LabelRequest
import com.tiyi.tiyi_app.dto.ListTagModel
import com.tiyi.tiyi_app.dto.LoginRequest
import com.tiyi.tiyi_app.dto.RechargeItemsModel
import com.tiyi.tiyi_app.dto.RegisterRequest
import com.tiyi.tiyi_app.dto.UserDetailsModel
import com.tiyi.tiyi_app.pojo.CorruptedApiException
import com.tiyi.tiyi_app.pojo.Result
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.net.ConnectException

fun HttpException.handleHttpException(): Result<Nothing> {
    return when (this.code()) {
        400 -> Result.BadRequest(this)
        401 -> Result.Unauthorized(this)
        500 -> Result.ServerInternalError(this)
        else -> throw CorruptedApiException()
    }
}

suspend fun <T> safeApiCall(apiCall: suspend () -> T): Result<T> = try {
    Result.Success(apiCall())
} catch (httpException: HttpException) {
    httpException.handleHttpException()
} catch (networkException: ConnectException) {
    Result.NetworkError(networkException)
}

class NetworkRepository(private val apiService: MusicApiService) {

    // User API
    suspend fun loginUser(loginRequest: LoginRequest): Result<CommonResponseModel> = try {
        val response = apiService.loginUser(loginRequest)
        if (response.code == 0) {
            TokenManager.setToken(response.data)
        }
        Result.Success(response)
    } catch (httpException: HttpException) {
        throw CorruptedApiException()
    } catch (networkException: ConnectException) {
        Result.NetworkError(networkException)
    }

    suspend fun registerUser(registerRequest: RegisterRequest): Result<CommonResponseModel> =
        safeApiCall { apiService.registerUser(registerRequest) }

    suspend fun getUserDetails(): Result<UserDetailsModel> =
        safeApiCall { apiService.getUserDetails() }

    suspend fun uploadAudio(file: RequestBody): Result<AudioUploadResponseModel> =
        safeApiCall { apiService.uploadAudio(file) }

    suspend fun labelAudio(labelRequest: LabelRequest): Result<CommonResponseModel> =
        safeApiCall { apiService.labelAudio(labelRequest) }

    suspend fun getAudioDetails(audioId: String): Result<AudioDetailsModel> =
        safeApiCall { apiService.getAudioDetails(audioId) }

    suspend fun deleteAudio(audioId: String): Result<CommonResponseModel> =
        safeApiCall { apiService.deleteAudio(audioId) }

    suspend fun listAudios(name: String? = null, tags: String? = null): Result<AudioListModel> =
        safeApiCall { apiService.listAudios(name, tags) }

    suspend fun listTags(): Result<ListTagModel> =
        safeApiCall { apiService.listTags() }

    suspend fun addTag(tagName: String): Result<CommonResponseModel> =
        safeApiCall { apiService.addTag(tagName) }

    suspend fun deleteTag(tagName: String): Result<ListTagModel> =
        safeApiCall { apiService.deleteTag(tagName) }

    suspend fun getOrderInfo(rechargeId: Int): Result<CommonResponseModel> =
        safeApiCall { apiService.getOrderInfo(rechargeId) }

    suspend fun getAnalysisItems(): Result<AnalysisItemsModel> =
        safeApiCall { apiService.getAnalysisItems() }

    suspend fun getRechargeItems(): Result<RechargeItemsModel> =
        safeApiCall { apiService.getRechargeItems() }

    suspend fun checkMusicCoinConsumption(itemNames: String): Result<ConsumptionCheckModel> =
        safeApiCall { apiService.checkMusicCoinConsumption(itemNames) }

    suspend fun getOrderInfoStr(rechargeId: String): Result<CommonResponseModel> =
        safeApiCall { apiService.getOrderInfoStr(rechargeId) }

    suspend fun uploadFile(file: RequestBody): Result<FileResponseModel> =
        safeApiCall { apiService.uploadFile(file) }

    suspend fun downloadFile(filename: String): Result<ResponseBody> =
        safeApiCall { apiService.downloadFile(filename) }
}