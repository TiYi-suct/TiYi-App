package com.tiyi.tiyi_app.data

import android.content.Context
import android.content.SharedPreferences

object TokenManager {
    private const val PREFS_NAME = "prefs" // SharedPreferences 名称
    private const val TOKEN_KEY = "auth_token" // 储存 token 的 key
    private lateinit var prefs: SharedPreferences

    // 初始化
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getToken(): String? {
        return prefs.getString(TOKEN_KEY, null)
    }

    fun setToken(token: String?) {
        prefs.edit().putString(TOKEN_KEY, token).apply()
    }
}