package com.tiyi.tiyi_app.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tiyi.tiyi_app.screen.LoginInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel: ViewModel() {
    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun setLoginInfo(info: LoginInfo) {
        _username.value = info.username
        _password.value = info.password
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun login() {
        // Login logic
        Log.d(TAG, "login: ${username.value} ${password.value}")
    }

    fun register() {
        // Register logic
        Log.d(TAG, "register: ${username.value} ${password.value}")
    }
}