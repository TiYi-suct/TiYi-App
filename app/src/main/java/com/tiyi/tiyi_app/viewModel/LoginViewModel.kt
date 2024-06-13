package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.dto.LoginRequest
import com.tiyi.tiyi_app.dto.RegisterRequest
import com.tiyi.tiyi_app.page.UserInfo
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class LoginStatus {
    data object Idle : LoginStatus()
    data object Success : LoginStatus()
    class Error(val message: String) : LoginStatus()
}

class LoginViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val tiyiApplication = application as TiyiApplication
    private val networkRepository = tiyiApplication.networkRepository

    private var _username = MutableStateFlow("")
    val username: StateFlow<String> = _username.asStateFlow()

    private var _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private var _loginStatus: MutableStateFlow<LoginStatus> = MutableStateFlow(LoginStatus.Idle)
    val loginStatus: StateFlow<LoginStatus> = _loginStatus.asStateFlow()


    fun setUserInfo(info: UserInfo) {
        _username.value = info.username
        _password.value = info.password
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }

    fun login() {
        // Login logic
        Log.d(TAG, "login: ${username.value} ${password.value}")
        viewModelScope.launch {
            val loginRequest = LoginRequest(username.value, password.value)
            when (val result = networkRepository.loginUser(loginRequest)) {
                is Result.Success -> {
                    _loginStatus.value = LoginStatus.Success
                }

                is Result.BadRequest -> {
                    _loginStatus.value = LoginStatus.Error(result.message)
                }

                else -> {
                    Log.e(TAG, "未知错误")
                }
            }
        }

    }

    fun register() {
        // Register logic
        Log.d(TAG, "register: ${username.value} ${password.value}")
        viewModelScope.launch {
            val request = RegisterRequest(username.value, password.value)
            when (val result = networkRepository.registerUser(request)) {
                is Result.Success -> {
                    login()
                }

                is Result.BadRequest -> {
                    _loginStatus.value = LoginStatus.Error(result.message)
                }

                else -> {
                    Log.e(TAG, "未知错误")
                }
            }
        }
    }
}