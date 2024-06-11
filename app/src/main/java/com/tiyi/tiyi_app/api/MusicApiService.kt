package com.tiyi.tiyi_app.api

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApiService {
    // User API start
    @POST("user/login")
    suspend fun loginUser(@Body loginRequest: LoginRequest): CommonResponseModel

    @POST("user/register")
    suspend fun registerUser(@Body registerRequest: RegisterRequest): CommonResponseModel

    @GET("user")
    suspend fun getUserDetails(): UserDetailsModel

    @Multipart
    @PUT("user/avatar")
    suspend fun updateAvatar(@Part avatar: MultipartBody.Part): CommonResponseModel

    // User API end

    // Music API start
    @POST("audio/upload")
    suspend fun uploadAudio(@Body file: RequestBody): AudioUploadResponseModel

    @PUT("audio/labeling")
    suspend fun labelAudio(@Body labelRequest: LabelRequest): CommonResponseModel

    @GET("audio")
    suspend fun getAudioDetails(@Query("audio_id") audioId: String): AudioDetailsModel

    @DELETE("audio")
    suspend fun deleteAudio(@Query("audio_id") audioId: String): CommonResponseModel

    @GET("audio/list")
    suspend fun listAudios(
        @Query("name") name: String?,
        @Query("tags") tags: String?
    ): AudioListModel

    // Music API end

    // Tag API start
    @POST("audio_tags")
    suspend fun addTag(@Query("tag_name") tagName: String): CommonResponseModel

    @GET("audio_tags")
    suspend fun listTags(): ListTagModel

    @DELETE("audio_tags")
    suspend fun deleteTag(@Query("tag_name") tagName: String): ListTagModel
    // Tag API end

    // Coin API start
    @GET("pay/order_info_str")
    suspend fun getOrderInfo(@Query("recharge_id") rechargeId: Int): CommonResponseModel

    @GET("analysis_item")
    suspend fun getAnalysisItems(): AnalysisItemsModel

    @GET("recharge_item")
    suspend fun getRechargeItems(): RechargeItemsModel

    @GET("analysis_item/consumption")
    suspend fun checkMusicCoinConsumption(@Query("item_names") itemNames: String): ConsumptionCheckModel

    @GET("pay/order_info_str")
    suspend fun getOrderInfoStr(@Query("recharge_id") rechargeId: String): CommonResponseModel
    // Coin API end

    // File API Start
    @POST("file")
    suspend fun uploadFile(@Body file: RequestBody): FileResponseModel

    @GET("file/{filename}")
    suspend fun downloadFile(@Path("filename") filename: String): ResponseBody
    // File API End
}