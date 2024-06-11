package com.tiyi.tiyi_app.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class LoginRequest(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String
)

@Serializable
data class RegisterRequest(
    @SerialName("username") val username: String,
    @SerialName("password") val password: String
)

@Serializable
data class UserDetailsModel(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: UserDetails
) {
    @Serializable
    data class UserDetails(
        @SerialName("username") val username: String,
        @SerialName("music_coin") val musicCoin: Int,
        @SerialName("avatar") val avatar: String?,
        @SerialName("signature") val signature: String
    )
}



