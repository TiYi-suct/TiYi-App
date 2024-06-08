package com.tiyi.tiyi_app.network

import com.tiyi.tiyi_app.model.AnalysisItemsModel
import com.tiyi.tiyi_app.model.AudioInfoListModel
import com.tiyi.tiyi_app.model.AudioInfoModel
import com.tiyi.tiyi_app.model.AudioUploadModel
import com.tiyi.tiyi_app.model.CommonResponseModel
import com.tiyi.tiyi_app.model.ConsumptionCheckModel
import com.tiyi.tiyi_app.model.FileResponseModel
import com.tiyi.tiyi_app.model.ListTagModel
import com.tiyi.tiyi_app.model.RechargeItemsModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MusicApiService {
    // User API start
    @GET("user/login")
    fun userLogin(): CommonResponseModel

    @POST("user/register")
    fun userRegister(): CommonResponseModel

    @GET("user")
    fun getUserInfo(): CommonResponseModel

    // User API end

    // Music API start
    @POST("audio/upload")
    fun uploadMusic(): AudioUploadModel

    @PUT("audio/labeling")
    fun labelingMusic(): CommonResponseModel

    @GET("audio")
    fun getMusicInfo(): AudioInfoModel

    @GET("audio/list")
    fun getMusicList(): AudioInfoListModel

    @DELETE
    fun deleteMusic(): CommonResponseModel
    // Music API end

    // Tag API start
    @POST("audio_tags")
    fun addTag(): CommonResponseModel

    @GET("audio_tags")
    fun listTag(): ListTagModel

    @DELETE("audio_tags")
    fun deleteTag(): ListTagModel
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