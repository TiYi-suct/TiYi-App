package com.tiyi.tiyi_app.pojo.analysis

import com.tiyi.tiyi_app.repository.NetworkRepository

abstract class AnalysisRequest<T>(
    val analysisId: Int,
    val startTime: Float? = null,
    val endTime: Float? = null,
) {
    abstract suspend fun analysis(
        networkRepository: NetworkRepository,
        onError: (String) -> Unit
    ): T?
}