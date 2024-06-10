package com.tiyi.tiyi_app.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.TiyiApplication
import com.tiyi.tiyi_app.data.MusicInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

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

    init {
        val fakeTags = listOf(
            "流行",
            "摇滚",
            "古典",
            "电子",
            "爵士",
            "民谣",
            "说唱",
            "轻音乐",
        )

        viewModelScope.launch {
            val result = tiyiApplication.networkRepository.listTags()
            Log.d(TAG, "test: $result")
            _tagList.value = result.data
        }

        fun generateRandomString(length: Int): String {
            val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
            return (1..length)
                .map { chars.random() }
                .joinToString("")
        }

        val fakeSongs = Array(100) {
            MusicInfo(
                it, generateRandomString(
                    Random(it).nextInt(15, 30)
                ),
                List(Random(it).nextInt(1, 5)) {
                    fakeTags.random()
                }
            )
        }

        _recentList.value = fakeSongs.toList()
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