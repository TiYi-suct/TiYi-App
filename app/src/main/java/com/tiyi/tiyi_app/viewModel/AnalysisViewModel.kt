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
import java.io.File

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
    private val _analysisCost = MutableStateFlow(0)
    val analysisCost = _analysisCost.asStateFlow()
    private val _allowed = MutableStateFlow(false)
    val allowed = _allowed.asStateFlow()

    private val _transpositionSteps = MutableStateFlow(2)
    val transpositionSteps = _transpositionSteps.asStateFlow()

    private val _mfccFactor = MutableStateFlow(5)
    val mfccFactor = _mfccFactor.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isDownloading = MutableStateFlow(false)
    val isDownloading = _isDownloading.asStateFlow()

    private var _sliceExtension: String = ""

    private val _filename = MutableStateFlow("")
    val filename = _filename.asStateFlow()


    init {
        fetchAnalysisItem()
        getExtension()
    }

    fun getExtension() {
        Log.d(TAG, "downloadAnalysisTarget")
        viewModelScope.launch {
            when (val result = networkRepository.listAudios(name = _sliceName.value)) {
                is Result.Success -> {
                    _sliceExtension =
                        result.data.data.find { it.name == _sliceName.value }?.extension ?: ""
                    if (_sliceExtension != "") {
                        _filename.value = String.format("%s.%s", _id.value, _sliceExtension)
                    } else {
                        submitError(message = result.message + "extension is empty")
                    }
                }

                else -> {
                    submitError(message = result.message)
                }
            }
        }
    }


    fun deleteCachedFile() {
        val filename = _sliceName.value
        val cacheDir = getApplication<Application>().cacheDir
        val file = File(cacheDir, filename)
        if (file.exists()) {
            val deleted = file.delete()
            if (deleted) {
                Log.d(TAG, "deleteCachedFile: File deleted successfully")
            } else {
                submitError(message = "Failed to delete file")
            }
        } else {
            Log.d(TAG, "deleteCachedFile: File not found")
        }
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
        queryAnalysisCost()
    }

    private fun queryAnalysisCost() {
        Log.d(TAG, "queryAnalysisCost")
        val selectedItems = _analysisItems.value.filterValues { it }.keys
        if (selectedItems.isEmpty()) {
            _analysisCost.value = 0
            return
        }
        viewModelScope.launch {
            val selectedNames = selectedItems.map { it.title }
            Log.d(TAG, "queryAnalysisCost: $selectedNames")
            when (val result = networkRepository.checkMusicCoinConsumption(selectedNames.joinToString(","))) {
                is Result.Success -> {
                    val response = result.data
                    Log.d(TAG, "queryAnalysisCost: $response")
                    if (response.code != 0) {
                        submitError(message = response.msg)
                        return@launch
                    }
                    _analysisCost.value = response.data.required
                    _allowed.value = response.data.allow
                }
                else -> {
                    submitError(message = result.message)
                }
            }
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

    fun selectAllAnalysisItems() {
        Log.d(TAG, "selectAllAnalysisItems")
        _analysisItems.value = _analysisItems.value.mapValues { true }
        queryAnalysisCost()
    }
}
