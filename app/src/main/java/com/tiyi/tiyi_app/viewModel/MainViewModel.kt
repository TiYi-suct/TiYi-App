package com.tiyi.tiyi_app.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun submitError(message: String) {
        _error.value = message
    }

    fun clearError() {
        _error.value = null
    }

    fun setLoading(loading: Boolean) {
        _loading.value = loading
    }

    fun toggleLoading() {
        _loading.value = !_loading.value
    }
}