package com.tiyi.tiyi_app.model

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {
    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun setUsername(username: String) {
        _username.value = username
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun login() {
        // Login logic
    }

    fun register() {
        // Register logic
    }
}