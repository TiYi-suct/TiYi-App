package com.tiyi.tiyi_app.pojo

import com.tiyi.tiyi_app.pojo.analysis.AnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.BpmAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.MelSpectrogramAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.MfccAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.SpectrogramAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.TranspositionAnalysisRequest
import java.io.Serializable

val analysisUrlOfId = mapOf(
    1 to "/analysis/bpm",
    2 to "/analysis/transposition",
    3 to "/analysis/mel_spectrogram",
    4 to "/analysis/spectrogram",
    5 to "/analysis/mfcc",
)

data class TransferAnalysisRequest(
    val audioId: String,
    val analysisItems: Map<AnalysisItemInfo, Boolean>,
    val transpositionSteps: Int? = null,
    val mfccFactor: Int? = null,
    val startTime: Float? = null,
    val endTime: Float? = null
) : Serializable

fun TransferAnalysisRequest.toAnalysisRequest(): List<AnalysisRequest<*>> {
    val analysisRequests = mutableListOf<AnalysisRequest<*>>()
    analysisItems.forEach { (analysisItemInfo, isSelected) ->
        if (isSelected) {
            when (analysisItemInfo.id) {
                1 -> {
                    analysisRequests.add(BpmAnalysisRequest(audioId, startTime, endTime))
                }

                2 -> {
                    analysisRequests.add(
                        TranspositionAnalysisRequest(
                            audioId,
                            transpositionSteps ?: 2,
                            startTime,
                            endTime
                        )
                    )
                }

                3 -> {
                    analysisRequests.add(MelSpectrogramAnalysisRequest(audioId, startTime, endTime))
                }

                4 -> {
                    analysisRequests.add(SpectrogramAnalysisRequest(audioId, startTime, endTime))
                }

                5 -> {
                    analysisRequests.add(
                        MfccAnalysisRequest(
                            audioId,
                            mfccFactor ?: 10,
                            startTime,
                            endTime
                        )
                    )
                }

                else -> {
                    throw IllegalArgumentException("Unknown analysis item id: ${analysisItemInfo.id}")
                }
            }
        }
    }
    return analysisRequests
}