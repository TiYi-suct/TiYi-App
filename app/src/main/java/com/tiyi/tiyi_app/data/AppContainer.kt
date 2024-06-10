package com.tiyi.tiyi_app.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tiyi.tiyi_app.network.MusicApiService
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit

@Suppress("unused")
object AppContainer {
    private val baseUrl = "http://dev-cn.your-api-server.com/"

    private val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()
    }

    // 创建携带 Token 的 Retrofit 实例
    private val retrofitAuth: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .client(client)
            .build()
    }

    private val musicApiService: MusicApiService by lazy {
        retrofitAuth.create(MusicApiService::class.java)
    }

    val networkRepository: NetworkRepository by lazy {
        NetworkRepository(apiService = musicApiService)
    }

    fun initialize(context: Context) {
        TokenManager.init(context)
    }

    private class AuthInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
                .build()
            return chain.proceed(newRequest)
        }
    }
}