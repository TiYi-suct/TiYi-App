package com.tiyi.tiyi_app.network

import com.tiyi.tiyi_app.model.AnalysisItemsModel
import com.tiyi.tiyi_app.model.AudioDetailsModel
import com.tiyi.tiyi_app.model.AudioListModel
import com.tiyi.tiyi_app.model.AudioUploadResponseModel
import com.tiyi.tiyi_app.model.CommonResponseModel
import com.tiyi.tiyi_app.model.ConsumptionCheckModel
import com.tiyi.tiyi_app.model.FileResponseModel
import com.tiyi.tiyi_app.model.LabelRequest
import com.tiyi.tiyi_app.model.ListTagModel
import com.tiyi.tiyi_app.model.LoginRequest
import com.tiyi.tiyi_app.model.RechargeItemsModel
import com.tiyi.tiyi_app.model.RegisterRequest
import com.tiyi.tiyi_app.model.UserDetailsModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

@Suppress("unused") // Hide from IDE warning
interface MusicApiService {
    // User API start
    @POST("user/login")
    fun loginUser(@Body loginRequest: LoginRequest): CommonResponseModel

    @POST("user/register")
    fun registerUser(@Body registerRequest: RegisterRequest): CommonResponseModel

    @GET("user")
    fun getUserDetails(): UserDetailsModel

    // User API end

    // Music API start
    @POST("audio/upload")
    fun uploadAudio(@Body file: RequestBody): AudioUploadResponseModel

    @PUT("audio/labeling")
    fun labelAudio(@Body labelRequest: LabelRequest): CommonResponseModel

    @GET("audio")
    fun getAudioDetails(@Query("audio_id") audioId: String): AudioDetailsModel

    @DELETE("audio")
    fun deleteAudio(@Query("audio_id") audioId: String): CommonResponseModel

    @GET("audio/list")
    fun listAudios(@Query("name") name: String?, @Query("tags") tags: String?): AudioListModel

    // Music API end

    // Tag API start
    @POST("audio_tags")
    fun addTag(@Query("tag_name") tagName: String): CommonResponseModel

    @GET("audio_tags")
    fun listTags(): ListTagModel

    @DELETE("audio_tags")
    fun deleteTag(@Query("tag_name") tagName: String): ListTagModel
    // Tag API end

    // Coin API start
    @GET("pay/order_info")
    fun getOrderInfo(@Query("recharge_id") rechargeId: String): CommonResponseModel

    @GET("analysis_item")
    fun getAnalysisItems(): AnalysisItemsModel

    @GET("recharge_item")
    fun getRechargeItems(): RechargeItemsModel

    @GET("analysis_item/consumption")
    fun checkMusicCoinConsumption(@Query("item_names") itemNames: String): ConsumptionCheckModel

    @GET("pay/order_info_str")
    fun getOrderInfoStr(@Query("recharge_id") rechargeId: String): CommonResponseModel
    // Coin API end

    // File API Start
    @POST("file")
    fun uploadFile(@Body file: RequestBody): FileResponseModel

    @GET("file/{filename}")
    fun downloadFile(@Path("filename") filename: String): ResponseBody
    // File API End
}