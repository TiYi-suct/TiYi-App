package com.tiyi.tiyi_app.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnalysisItemsModel(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: List<AnalysisItem>
) {
    @Serializable
    data class AnalysisItem(
        @SerialName("id") val id: Int,
        @SerialName("name") val name: String,
        @SerialName("price") val price: Int
    )
}

@Serializable
data class RechargeItemsModel(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: List<RechargeItem>
) {
    @Serializable
    data class RechargeItem(
        @SerialName("id") val id: Int,
        @SerialName("title") val title: String,
        @SerialName("amount") val amount: Int,
        @SerialName("price") val price: Int
    )
}

@Serializable
data class ConsumptionCheckModel(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: ConsumptionData
) {
    @Serializable
    data class ConsumptionData(
        @SerialName("allow") val allow: Boolean,
        @SerialName("required") val required: Int,
        @SerialName("user_coins") val userCoins: Int
    )
}