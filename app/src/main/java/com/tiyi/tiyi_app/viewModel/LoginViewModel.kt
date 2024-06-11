package com.tiyi.tiyi_app.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tiyi.tiyi_app.page.LoginInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class LoginStatus {
    data object Idle: LoginStatus()
    data object Success: LoginStatus()
    class Error(val message: String): LoginStatus()
}

class LoginViewModel: ViewModel() {
    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private var _loginSuccess: MutableStateFlow<LoginStatus> = MutableStateFlow(LoginStatus.Idle)
    val loginStatus: StateFlow<LoginStatus> = _loginSuccess.asStateFlow()

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
        _loginSuccess.value = LoginStatus.Success
    }

    fun register() {
        // Register logic
        Log.d(TAG, "register: ${username.value} ${password.value}")
        _loginSuccess.value = LoginStatus.Error("Temporary error message")
    }
}