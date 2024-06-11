package com.tiyi.tiyi_app.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioUploadResponseModel(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: AudioInfo
) {
    @Serializable
    data class AudioInfo(
        @SerialName("audio_id") val audioId: String,
        @SerialName("url") val url: String
    )
}

@Serializable
data class LabelRequest(
    @SerialName("audio_id") val audioId: String,
    @SerialName("tags") val tags: String
)

@Serializable
data class AudioDetailsModel(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: AudioDetails
)

@Serializable
data class AudioListModel(
    @SerialName("code") val code: Int,
    @SerialName("msg") val msg: String,
    @SerialName("data") val data: List<AudioDetails>
)

@Serializable
data class AudioDetails(
    @SerialName("audio_id") val audioId: String,
    @SerialName("name") val name: String,
    @SerialName("extension") val extension: String,
    @SerialName("url") val url: String,
    @SerialName("tags") val tags: List<String>,
    @SerialName("cover") val cover: String?,
    @SerialName("username") val username: String
)

@Serializable
data class AudioUpdateRequestModel(
    @SerialName("audio_id") val audioId: String,
    @SerialName("tags") val tags: String? = null,
    @SerialName("cover") val cover: String? = null,
    @SerialName("description") val description: String? = null
)