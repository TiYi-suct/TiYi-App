package com.tiyi.tiyi_app.data

import com.tiyi.tiyi_app.dto.LoginRequest
import com.tiyi.tiyi_app.dto.RegisterRequest
import com.tiyi.tiyi_app.network.MusicApiService
import com.tiyi.tiyi_app.dto.CommonResponseModel
import com.tiyi.tiyi_app.dto.UserDetailsModel

@Suppress("unused")
class UserRepository(private val apiService: MusicApiService) {
    suspend fun loginUser(loginRequest: LoginRequest): CommonResponseModel {
        return apiService.loginUser(loginRequest)
    }

    suspend fun registerUser(registerRequest: RegisterRequest): CommonResponseModel {
        return apiService.registerUser(registerRequest)
    }

    suspend fun getUserDetails(): UserDetailsModel {
        return apiService.getUserDetails()
    }
}