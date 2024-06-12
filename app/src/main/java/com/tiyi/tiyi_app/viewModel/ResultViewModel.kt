package com.tiyi.tiyi_app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.pojo.TransferAnalysisRequest
import com.tiyi.tiyi_app.pojo.AnalysisResult
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    application: Application
) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "ResultViewModel"
    }

    private val tiyiApplication = application as TiyiApplication
    val networkRepository = tiyiApplication.networkRepository

    private val _Transfer_analysisRequest = MutableStateFlow<TransferAnalysisRequest?>(null)
    val analysisRequest = _Transfer_analysisRequest.asStateFlow()
    private val _analysisResult = MutableStateFlow(AnalysisResult())
    val analysisResult = _analysisResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun analysisMelSpec(audioId: String, startTime: Float? = null, endTime: Float? = null) {
        viewModelScope.launch {
            when (val result = networkRepository.analysisMelSpec(audioId, startTime, endTime)) {
                is Result.Success -> {
                    val response = result.data
                    if (response.code != 0) {
                        submitError(response.msg)
                        return@launch
                    }
                    _analysisResult.value = _analysisResult.value.copy(
                        melSpectrogram = response.data
                    )
                }
                else -> {
                    submitError(result.message)
                }
            }
        }
    }

    fun submitError(error: String) {
        _error.value = error
    }

    fun clearError(error: String) {
        _error.value = null
    }
}