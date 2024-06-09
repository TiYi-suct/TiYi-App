package com.tiyi.tiyi_app.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.tiyi.tiyi_app.network.MusicApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

@Suppress("unused")
object AppContainer {
    private val baseUrl = "http://dev-cn.your-api-server.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .baseUrl(baseUrl)
            .build()
    }

    private val musicApiService: MusicApiService by lazy {
        retrofit.create(MusicApiService::class.java)
    }

    val userRepository: UserRepository by lazy {
        UserRepository(musicApiService)
    }
}