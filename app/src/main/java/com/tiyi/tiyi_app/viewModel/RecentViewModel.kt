package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.dto.LabelRequest
import com.tiyi.tiyi_app.pojo.CorruptedApiException
import com.tiyi.tiyi_app.pojo.MusicInfo
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RecentViewModel(
    application: Application
) : AndroidViewModel(application) {
    companion object {
        private const val TAG = "RecentViewModel"
    }

    private val tiyiApplication = application as TiyiApplication
    private val networkRepository = tiyiApplication.networkRepository

    private val _tagList = MutableStateFlow(listOf<String>())
    val tagList = _tagList.asStateFlow()

    private val _recentList = MutableStateFlow(listOf<MusicInfo>())
    val recentList = _recentList.asStateFlow()

    private val _selectedTagList = MutableStateFlow(listOf<String>())
    val selectedTagList = _selectedTagList.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch {
            _loading.value = true
            fetchTagAndRecentList()
            _loading.value = false
        }
    }

    private fun submitError(message: String) {
        _error.value = message
        Log.d(TAG, "submitError: $message")
    }

    fun clearError() {
        _error.value = null
    }

    private suspend fun fetchTagAndRecentList() {
        fetchTagList()
        fetchAudioList()
    }

    fun refreshAudioList(
        name: String? = null,
        tags: List<String>? = null
    ) = viewModelScope.launch {
        _loading.value = true
        clearError()
        fetchAudioList(name, tags)
        _loading.value = false
    }

    private suspend fun fetchAudioList(
        name: String? = null,
        tags: List<String>? = null
    ) {
        when (val result = networkRepository.listAudios(name, tags?.joinToString(","))) {
            is Result.Success -> {
                val response = result.data
                if (response.code != 0)
                    throw CorruptedApiException()
                _recentList.value = response.data.map {
                    MusicInfo(
                        id = it.audioId,
                        title = it.name,
                        tags = it.tags,
                    )
                }
            }
            else -> submitError(result.message)
        }
    }

    private suspend fun fetchTagList() {
        when (val result = networkRepository.listTags()) {
            is Result.Success -> {
                val response = result.data
                if (response.code != 0)
                    throw CorruptedApiException()
                _tagList.value = response.data
            }

            else -> submitError(result.message)
        }
    }

    fun addTag(tag: String) {
        Log.d(TAG, "addTag: $tag")
        viewModelScope.launch {
            when (val result = networkRepository.addTag(tag)) {
                is Result.Success -> {
                    val response = result.data
                    if (response.code != 0) {
                        submitError(response.msg)
                        return@launch
                    }
                    _tagList.value += tag
                }
                else -> submitError(result.message)
            }
        }
    }

    fun editTagFor(musicInfo: MusicInfo, newTags: List<String>) {
        Log.d(TAG, "editTagFor: $musicInfo, $newTags")
        viewModelScope.launch {
            when (val result = networkRepository.labelAudio(
                LabelRequest(
                    musicInfo.id,
                    newTags.joinToString(","
                )
            ))) {
                is Result.Success -> {
                    val response = result.data
                    if (response.code != 0) {
                        submitError(response.msg)
                        return@launch
                    }
                    _recentList.value = _recentList.value.map {
                        if (it.id == musicInfo.id) {
                            it.copy(tags = newTags)
                        } else {
                            it
                        }
                    }
                }
                else -> submitError(result.message)
            }
        }
    }

    fun deleteMusic(musicInfo: MusicInfo) {
        Log.d(TAG, "deleteMusic: $musicInfo")
    }

    fun analysisMusic(musicInfo: MusicInfo) {
        Log.d(TAG, "analysisMusic: $musicInfo")
    }

    fun updateSelectedTagList(tagList: List<String>) {
        Log.d(TAG, "updateSelectedTagList: $tagList")
        refreshAudioList(tags = tagList)
    }

    fun searchMusic(query: String) {
        Log.d(TAG, "searchMusic: $query")
        refreshAudioList(query)
    }
}