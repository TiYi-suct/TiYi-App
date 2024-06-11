package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.pojo.AnalysisItemInfo
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AnalysisViewModel(
    application: Application
) : AndroidViewModel(application) {
    companion object {
        val TAG = AnalysisViewModel::class.simpleName
    }

    private val tiyiApplication = application as TiyiApplication
    private val networkRepository = tiyiApplication.networkRepository

    private val _sliceName = MutableStateFlow("")
    val sliceName = _sliceName.asStateFlow()
    private val _id = MutableStateFlow("")
    val id = _id.asStateFlow()

    private val _analysisItems = MutableStateFlow<Map<AnalysisItemInfo, Boolean>>(emptyMap())
    val analysisItems = _analysisItems.asStateFlow()

    private val _transpositionSteps = MutableStateFlow(2)
    val transpositionSteps = _transpositionSteps.asStateFlow()

    private val _mfccFactor = MutableStateFlow(20)
    val mfccFactor = _mfccFactor.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        fetchAnalysisItem()
    }

    fun updateAnalysisTarget(title: String, id: String) {
        Log.d(TAG, "updateAnalysisTarget: $title, $id")
        _sliceName.value = title
        _id.value = id
    }

    fun fetchAnalysisItem() {
        Log.d(TAG, "fetchAnalysisItem")
        viewModelScope.launch {
            when (val result = networkRepository.getAnalysisItems()) {
                is Result.Success -> {
                    val response = result.data
                    Log.d(TAG, "fetchAnalysisItem: $response")
                    if (response.code != 0) {
                        submitError(message = response.msg)
                        return@launch
                    }
                    _analysisItems.value = response.data.associate {
                        AnalysisItemInfo(
                            id = it.id,
                            title = it.name,
                            description = it.description,
                            price = it.price
                        ) to false
                    }
                }
                else -> {
                    submitError(message = result.message)
                }
            }
        }
    }

    fun updateAnalysisItemSelection(item: AnalysisItemInfo, isSelected: Boolean) {
        Log.d(TAG, "updateAnalysisItemSelection: $item, $isSelected")
        _analysisItems.value = _analysisItems.value.toMutableMap().apply {
            this[item] = isSelected
        }
    }

    fun submitError(message: String) {
        _error.value = message
        Log.d(TAG, "submitError: $message")
    }

    fun clearError() {
        _error.value = null
    }

    fun updateTranspositionSteps(steps: Int) {
        Log.d(TAG, "updateTranspositionSteps: $steps")
        _transpositionSteps.value = steps
    }

    fun updateMfccFactor(factor: Int) {
        Log.d(TAG, "updateMfccFactor: $factor")
        _mfccFactor.value = factor
    }
}