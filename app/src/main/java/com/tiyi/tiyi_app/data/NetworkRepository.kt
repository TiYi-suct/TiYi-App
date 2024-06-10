package com.tiyi.tiyi_app.data

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
import com.tiyi.tiyi_app.network.MusicApiService
import okhttp3.RequestBody
import okhttp3.ResponseBody

@Suppress("unused")
class NetworkRepository(
    private val apiService: MusicApiService
) {

    // User API
    fun loginUser(loginRequest: LoginRequest): CommonResponseModel {
        val response = apiService.loginUser(loginRequest)
        if (response.code == 200) {
            TokenManager.setToken(response.data)
        }
        return response
    }

    fun registerUser(registerRequest: RegisterRequest): CommonResponseModel {
        return apiService.registerUser(registerRequest)
    }

    fun getUserDetails(): UserDetailsModel {
        return apiService.getUserDetails()
    }

    // Music API
    fun uploadAudio(file: RequestBody): AudioUploadResponseModel {
        return apiService.uploadAudio(file)
    }

    fun labelAudio(labelRequest: LabelRequest): CommonResponseModel {
        return apiService.labelAudio(labelRequest)
    }

    fun getAudioDetails(audioId: String): AudioDetailsModel {
        return apiService.getAudioDetails(audioId)
    }

    fun deleteAudio(audioId: String): CommonResponseModel {
        return apiService.deleteAudio(audioId)
    }

    fun listAudios(name: String?, tags: String?): AudioListModel {
        return apiService.listAudios(name, tags)
    }

    // Tag API
    fun addTag(tagName: String): CommonResponseModel {
        return apiService.addTag(tagName)
    }

    fun listTags(): ListTagModel {
        return apiService.listTags()
    }

    fun deleteTag(tagName: String): ListTagModel {
        return apiService.deleteTag(tagName)
    }

    // Coin API
    fun getOrderInfo(rechargeId: String): CommonResponseModel {
        return apiService.getOrderInfo(rechargeId)
    }

    fun getAnalysisItems(): AnalysisItemsModel {
        return apiService.getAnalysisItems()
    }

    fun getRechargeItems(): RechargeItemsModel {
        return apiService.getRechargeItems()
    }

    fun checkMusicCoinConsumption(itemNames: String): ConsumptionCheckModel {
        return apiService.checkMusicCoinConsumption(itemNames)
    }

    fun getOrderInfoStr(rechargeId: String): CommonResponseModel {
        return apiService.getOrderInfoStr(rechargeId)
    }

    // File API
    fun uploadFile(file: RequestBody): FileResponseModel {
        return apiService.uploadFile(file)
    }

    fun downloadFile(filename: String): ResponseBody {
        return apiService.downloadFile(filename)
    }
}