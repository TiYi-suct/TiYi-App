package com.tiyi.tiyi_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonResponseModel(
    @SerialName("code")
    val code: Int,

    @SerialName("msg")
    val msg: String,

    @SerialName("data")
    val data: String
)
