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
import okhttp3.RequestBody
import okhttp3.ResponseBody

@Suppress("unused")
class NetworkRepository(private val apiService: MusicApiService) {

    // User API
    suspend fun loginUser(loginRequest: LoginRequest): CommonResponseModel {
        val response = apiService.loginUser(loginRequest)
        if (response.code == 0) {
            TokenManager.setToken(response.data)
        }
        return response
    }

    suspend fun registerUser(registerRequest: RegisterRequest): CommonResponseModel {
        return apiService.registerUser(registerRequest)
    }

    suspend fun getUserDetails(): UserDetailsModel {
        return apiService.getUserDetails()
    }

    // Music API
    suspend fun uploadAudio(file: RequestBody): AudioUploadResponseModel {
        return apiService.uploadAudio(file)
    }

    suspend fun labelAudio(labelRequest: LabelRequest): CommonResponseModel {
        return apiService.labelAudio(labelRequest)
    }

    suspend fun getAudioDetails(audioId: String): AudioDetailsModel {
        return apiService.getAudioDetails(audioId)
    }

    suspend fun deleteAudio(audioId: String): CommonResponseModel {
        return apiService.deleteAudio(audioId)
    }

    suspend fun listAudios(name: String? = null, tags: String? = null): AudioListModel {
        return apiService.listAudios(name, tags)
    }

    // Tag API
    suspend fun addTag(tagName: String): CommonResponseModel {
        return apiService.addTag(tagName)
    }

    suspend fun listTags(): ListTagModel {
        return apiService.listTags()
    }

    suspend fun deleteTag(tagName: String): ListTagModel {
        return apiService.deleteTag(tagName)
    }

    // Coin API
    suspend fun getOrderInfo(rechargeId: String): CommonResponseModel {
        return apiService.getOrderInfo(rechargeId)
    }

    suspend fun getAnalysisItems(): AnalysisItemsModel {
        return apiService.getAnalysisItems()
    }

    suspend fun getRechargeItems(): RechargeItemsModel {
        return apiService.getRechargeItems()
    }

    suspend fun checkMusicCoinConsumption(itemNames: String): ConsumptionCheckModel {
        return apiService.checkMusicCoinConsumption(itemNames)
    }

    suspend fun getOrderInfoStr(rechargeId: String): CommonResponseModel {
        return apiService.getOrderInfoStr(rechargeId)
    }

    // File API
    suspend fun uploadFile(file: RequestBody): FileResponseModel {
        return apiService.uploadFile(file)
    }

    suspend fun downloadFile(filename: String): ResponseBody {
        return apiService.downloadFile(filename)
    }
}