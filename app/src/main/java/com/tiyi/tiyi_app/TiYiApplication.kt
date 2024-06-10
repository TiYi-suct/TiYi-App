package com.tiyi.tiyi_app

import android.app.Application
import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tiyi.tiyi_app.data.NetworkRepository
import com.tiyi.tiyi_app.data.TokenManager
import com.tiyi.tiyi_app.network.MusicApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

class MyApplication : Application() {
    lateinit var networkRepository: NetworkRepository
        private set

    override fun onCreate() {
        super.onCreate()
        initialize(this)
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
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl("http://dev-cn.your-api-server.com/")
            .client(client)
            .build()

        // Create MusicApiService
        val musicApiService = retrofitAuth.create(MusicApiService::class.java)

        // Initialize NetworkRepository
        networkRepository = NetworkRepository(musicApiService)
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