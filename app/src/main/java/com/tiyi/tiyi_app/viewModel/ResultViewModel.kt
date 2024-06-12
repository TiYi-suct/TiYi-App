package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.pojo.AnalysisResult
import com.tiyi.tiyi_app.pojo.Result
import com.tiyi.tiyi_app.pojo.analysis.AnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.MelSpectrogramAnalysisRequest
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

    private val _sliceName = MutableStateFlow("")
    val sliceName = _sliceName.asStateFlow()

    private val _analysisRequest = MutableStateFlow<List<AnalysisRequest<*>>>(emptyList())
    val analysisRequest = _analysisRequest.asStateFlow()

    private val _analysisResult = MutableStateFlow(AnalysisResult())
    val analysisResult = _analysisResult.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun updateSliceName(sliceName: String) {
        _sliceName.value = sliceName
    }

    fun updateAnalysisRequest(requests: List<AnalysisRequest<*>>) {
        _analysisRequest.value = requests
    }

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

    fun take(melSpectrogramAnalysisRequest: MelSpectrogramAnalysisRequest): State<String> {
        val resultUrl = mutableStateOf("")
        Log.d(TAG, "take: $melSpectrogramAnalysisRequest")
        viewModelScope.launch {
            val result = melSpectrogramAnalysisRequest.analysis(networkRepository) {
                submitError(it)
            }
            if (result != null) {
                resultUrl.value = result
            }
        }
        return resultUrl
    }

    fun submitError(error: String) {
        _error.value = error
    }

    fun clearError(error: String) {
        _error.value = null
    }
}