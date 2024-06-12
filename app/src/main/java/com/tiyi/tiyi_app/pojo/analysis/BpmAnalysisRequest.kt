package com.tiyi.tiyi_app.pojo.analysis

import com.tiyi.tiyi_app.pojo.Result
import com.tiyi.tiyi_app.repository.NetworkRepository

class BpmAnalysisRequest(
    audioId: String,
    startTime: Float? = null,
    endTime: Float? = null,
) : AnalysisRequest<Float>(1, audioId, startTime, endTime
) {
    override suspend fun analysis(
        networkRepository: NetworkRepository,
        onError: (String) -> Unit
    ): Float? {
        return when (val result = networkRepository.analysisBPM(audioId, startTime, endTime)) {
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