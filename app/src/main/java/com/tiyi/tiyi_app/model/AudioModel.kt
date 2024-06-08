package com.tiyi.tiyi_app.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioUploadModel(
    @SerialName("code")
    val code: Int,

    @SerialName("msg")
    val msg: String,

    @SerialName("data")
    val data: AudioUploadDataModel
)

@Serializable
data class AudioUploadDataModel(
    @SerialName("audio_id")
    val audioId: String,

    @SerialName("url")
    val url: String,
)

@Serializable
data class AudioInfoModel(
    @SerialName("code")
    val code: Int,

    @SerialName("msg")
    val msg: String,

    @SerialName("data")
    val data: AudioInfoDataModel
)

@Serializable
data class AudioInfoDataModel(
    @SerialName("audio_id")
    val audioId: String,

    @SerialName("name")
    val name: String,

    @SerialName("extension")
    val extension: String,

    @SerialName("url")
    val url: String,

    @SerialName("tags")
    val tags: List<String>,

    @SerialName("cover")
    val cover: String?,

    @SerialName("username")
    val username: String
)

@Serializable
data class AudioInfoListModel(
    @SerialName("code")
    val code: Int,

    @SerialName("msg")
    val msg: String,

    @SerialName("data")
    val data: List<AudioInfoDataModel>
)