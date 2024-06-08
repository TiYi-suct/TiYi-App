package com.tiyi.tiyi_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseModel(
    @SerialName("code")
    val code: Int,

    @SerialName("msg")
    val msg: String,

    @SerialName("data")
    val data: String
)

@Serializable
data class UserInfoModel(
    @SerialName("username")
    val username: String,

    @SerialName("music_coin")
    val musicCoin: Int,

    @SerialName("avatar")
    val avatar: String?,

    @SerialName("signature")
    val signature: String
)

