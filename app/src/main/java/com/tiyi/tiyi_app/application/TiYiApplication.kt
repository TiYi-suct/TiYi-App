package com.tiyi.tiyi_app.application

import android.app.Application
import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tiyi.tiyi_app.api.MusicApiService
import com.tiyi.tiyi_app.config.getServiceUrl
import com.tiyi.tiyi_app.dto.LoginRequest
import com.tiyi.tiyi_app.repository.NetworkRepository
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

class TiyiApplication : Application() {
    lateinit var networkRepository: NetworkRepository
        private set

    override fun onCreate() {
        super.onCreate()
        initialize(this)
    }

    val json = Json {
        ignoreUnknownKeys = true
    }

    private fun initialize(context: Context) {
        // 初始化 TokenManager
        TokenManager.init(context)

        // 使用 AuthInterceptor 创建 OkHttpClient
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        // 创建 retrofitAuth 实例
        val retrofitAuth = Retrofit.Builder()
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(getServiceUrl(this))
            .client(client)
            .build()

        // Create MusicApiService
        val musicApiService = retrofitAuth.create(MusicApiService::class.java)

        // Initialize NetworkRepository
        networkRepository = NetworkRepository(musicApiService)

        loginDebugAccount(true)
    }

    private fun loginDebugAccount(alwaysLoginAgain: Boolean = false) = runBlocking {
        if (!alwaysLoginAgain && TokenManager.getToken() != null)
            return@runBlocking
        Log.d("login", "loginDebugAccount: before login")
        val response = networkRepository.loginUser(
            LoginRequest(
                "maskira",
                "0717"
            )
        )
        Log.d("login", "loginDebugAccount: $response")
    }

    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "${TokenManager.getToken()}")
                .build()
            return chain.proceed(newRequest)
        }
    }
}