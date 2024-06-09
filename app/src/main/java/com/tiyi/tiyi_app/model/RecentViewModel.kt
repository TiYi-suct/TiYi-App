package com.tiyi.tiyi_app.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.tiyi.tiyi_app.data.MusicInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RecentViewModel: ViewModel() {
    companion object {
        private const val TAG = "RecentViewModel"
    }

    private val _tagList = MutableStateFlow(listOf<String>())
    val tagList = _tagList.asStateFlow()

    private val _recentList = MutableStateFlow(listOf<MusicInfo>())
    val recentList = _recentList.asStateFlow()

    fun addTag(tag: String) {
        Log.d(TAG, "addTag: $tag")
    }

    fun removeTag(tag: String) {
        Log.d(TAG, "removeTag: $tag")
    }

    fun fetchRecentList() {
        Log.d(TAG, "fetchRecentList")
    }

    fun searchRecentList(query: String) {
        Log.d(TAG, "searchRecentList: $query")
    }
}