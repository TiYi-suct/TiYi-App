package com.tiyi.tiyi_app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.pojo.AnalysisResult
import kotlinx.coroutines.flow.MutableStateFlow

class ResultViewModel(
    val application: Application
): AndroidViewModel(application){
    companion object {
        private const val TAG = "ResultViewModel"
    }
    private val tiyiApplication = application as TiyiApplication
    val networkRepository = tiyiApplication.networkRepository

    private val _analysisResult = MutableStateFlow(AnalysisResult())
    val analysisResult = _analysisResult


}