package com.tiyi.tiyi_app.pojo

import java.io.Serializable

val analysisUrlOfId = mapOf(
    1 to "/analysis/bpm",
    2 to "/analysis/transposition",
    3 to "/analysis/mel_spectrogram",
    4 to "/analysis/spectrogram",
    5 to "/analysis/mfcc",
)

data class AnalysisRequest(
    val audioId: String,
    val analysisItems: Map<AnalysisItemInfo, Boolean>,
    val transpositionSteps: Int? = null,
    val mfccFactor: Int? = null,
    val startTime: Float? = null,
    val endTime: Float? = null
) : Serializable