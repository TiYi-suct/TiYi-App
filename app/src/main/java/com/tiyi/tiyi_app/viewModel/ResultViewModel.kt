package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.pojo.AnalysisResult
import com.tiyi.tiyi_app.pojo.analysis.AnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.BpmAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.MelSpectrogramAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.MfccAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.SpectrogramAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.TranspositionAnalysisRequest
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

    fun take(mfccAnalysisRequest: MfccAnalysisRequest): State<String?> {
        val resultUrl = mutableStateOf<String?>(null)
        Log.d(TAG, "take: $mfccAnalysisRequest")
        viewModelScope.launch {
            val result = mfccAnalysisRequest.analysis(networkRepository) {
                submitError(it)
            }
            if (result != null) {
                resultUrl.value = result
            }
        }
        return resultUrl
    }

    fun take(transpositionAnalysisRequest: TranspositionAnalysisRequest): State<String?> {
        val resultUrl = mutableStateOf<String?>(null)
        Log.d(TAG, "take: $transpositionAnalysisRequest")
        viewModelScope.launch {
            val result = transpositionAnalysisRequest.analysis(networkRepository) {
                submitError(it)
            }
            if (result != null) {
                resultUrl.value = result
            }
        }
        return resultUrl
    }

    fun take(bpmAnalysisRequest: BpmAnalysisRequest): State<Float?> {
        val bpm = mutableStateOf<Float?>(null)
        Log.d(TAG, "take: $bpmAnalysisRequest")
        viewModelScope.launch {
            val result = bpmAnalysisRequest.analysis(networkRepository) {
                submitError(it)
            }
            if (result != null) {
                bpm.value = result
            }
        }
        return bpm
    }

    fun take(spectrogramAnalysisRequest: SpectrogramAnalysisRequest): State<String?> {
        val resultUrl = mutableStateOf<String?>(null)
        Log.d(TAG, "take: $spectrogramAnalysisRequest")
        viewModelScope.launch {
            val result = spectrogramAnalysisRequest.analysis(networkRepository) {
                submitError(it)
            }
            if (result != null) {
                resultUrl.value = result
            }
        }
        return resultUrl
    }

    fun take(melSpectrogramAnalysisRequest: MelSpectrogramAnalysisRequest): State<String?> {
        val resultUrl = mutableStateOf<String?>(null)
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