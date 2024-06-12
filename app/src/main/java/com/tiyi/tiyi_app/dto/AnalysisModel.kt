package com.tiyi.tiyi_app.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SpectrogramRequest(
    @SerialName("audio_id")
    val audioId: String,
    @SerialName("start_time")
    val startTime: Float = 0f,
    @SerialName("end_time")
    val endTime: Float = -1f,
)

@Serializable
data class BPMRequest(
    @SerialName("audio_id")
    val audioId: String,
    @SerialName("start_time")
    val startTime: Float = 0f,
    @SerialName("end_time")
    val endTime: Float = -1f,
)

@Serializable
data class TranspositionRequest(
    @SerialName("audio_id")
    val audioId: String,
    @SerialName("start_time")
    val startTime: Float = 0f,
    @SerialName("end_time")
    val endTime: Float = -1f,
    @SerialName("n_steps")
    val steps: Int = 2,
)

@Serializable
data class MfccRequest(
    @SerialName("audio_id")
    val audioId: String,
    @SerialName("start_time")
    val startTime: Float = 0f,
    @SerialName("end_time")
    val endTime: Float = -1f,
    @SerialName("n_mfcc")
    val factor: Int = 20,
)

