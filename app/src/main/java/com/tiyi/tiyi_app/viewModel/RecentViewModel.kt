package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.pojo.MusicInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecentViewModel(
    application: Application
): AndroidViewModel(application) {
    companion object {
        private const val TAG = "RecentViewModel"
    }

    private val tiyiApplication = application as TiyiApplication

    private val _tagList = MutableStateFlow(listOf<String>())
    val tagList = _tagList.asStateFlow()

    private val _recentList = MutableStateFlow(listOf<MusicInfo>())
    val recentList = _recentList.asStateFlow()

    private val _selectedTagList = MutableStateFlow(listOf<String>())
    val selectedTagList = _selectedTagList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            _loading.value = true
            val result = tiyiApplication.networkRepository.listTags()
            Log.d(TAG, "test: $result")
            _tagList.value = result.data
            _loading.value = false
        }

        fun generateRandomString(length: Int): String {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { chars.random() }
                .joinToString("")
        }
    }

    fun addTag(tag: String) {
        Log.d(TAG, "addTag: $tag")
    }

    fun editTagFor(musicInfo: MusicInfo, newTags: List<String>) {
        Log.d(TAG, "editTagFor: $musicInfo, $newTags")
    }

    fun deleteMusic(musicInfo: MusicInfo) {
        Log.d(TAG, "deleteMusic: $musicInfo")
    }

    fun analysisMusic(musicInfo: MusicInfo) {
        Log.d(TAG, "analysisMusic: $musicInfo")
    }

    fun updateSelectedTagList(tagList: List<String>) {
        Log.d(TAG, "updateSelectedTagList: $tagList")
        _recentList.value
    }

    fun searchMusic(query: String) {
        Log.d(TAG, "searchMusic: $query")
        _recentList.value
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