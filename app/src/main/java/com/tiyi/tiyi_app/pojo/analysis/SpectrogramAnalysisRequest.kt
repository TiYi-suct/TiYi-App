package com.tiyi.tiyi_app.pojo.analysis

import com.tiyi.tiyi_app.pojo.Result
import com.tiyi.tiyi_app.repository.NetworkRepository

class SpectrogramAnalysisRequest(
    audioId: String,
    startTime: Float? = null,
    endTime: Float? = null,
) : AnalysisRequest<String>(4, audioId, startTime, endTime) {
    override suspend fun analysis(
        networkRepository: NetworkRepository,
        onError: (String) -> Unit
    ): String? {
        return when (val result = networkRepository.analysisSpec(audioId, startTime, endTime)) {
            is Result.Success -> {
                val response = result.data
                if (response.code != 0) {
                    onError(response.msg)
                    return null
                }
                response.data
            }
            else -> {
                onError(result.message)
                null
            }
        }
    }
}