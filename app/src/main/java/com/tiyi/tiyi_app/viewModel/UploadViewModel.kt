package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.dto.AudioUpdateRequestModel
import com.tiyi.tiyi_app.page.getFileName
import com.tiyi.tiyi_app.pojo.CorruptedApiException
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class UploadViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val tiyiApplication = application as TiyiApplication
    private val networkRepository = tiyiApplication.networkRepository

    var selectedFileItems by mutableStateOf(listOf<Pair<String, Uri>>())
        private set

    var uploading by mutableStateOf(false)
        private set

    var uploadSuccess by mutableStateOf<Map<Uri, Boolean>>(emptyMap())
        private set

    var audioDescriptions by mutableStateOf<Map<Uri, String>>(emptyMap())
        private set

    var selectedTags by mutableStateOf<Map<Uri, List<String>>>(emptyMap())
        private set

    private val _tagList = MutableStateFlow(listOf<String>())
    val tagList = _tagList.asStateFlow()

    fun updateSelectedFileItems(items: List<Pair<String, Uri>>) {
        selectedFileItems = items
    }

    fun updateUploading(isUploading: Boolean) {
        uploading = isUploading
    }

    fun updateUploadSuccess(uri: Uri, success: Boolean) {
        uploadSuccess = uploadSuccess.toMutableMap().apply { put(uri, success) }
    }

    fun updateSelectedTags(uri: Uri, tags: List<String>) {
        selectedTags = selectedTags.toMutableMap().apply { put(uri, tags) }
    }

    init {
        viewModelScope.launch {
            fetchTagList()
        }
    }

    fun updateAudioDescription(uri: Uri, description: String) {
        val updatedDescriptions = audioDescriptions.toMutableMap()
        if (description.isEmpty()) {
            updatedDescriptions.remove(uri)
        } else {
            updatedDescriptions[uri] = description
        }
        audioDescriptions = updatedDescriptions
    }

    fun uploadFile(uri: Uri, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            try {
                context.contentResolver.openFileDescriptor(uri, "r", null)
                    ?.use { parcelFileDescriptor ->
                        FileInputStream(parcelFileDescriptor.fileDescriptor).use { inputStream ->
                            val file = File(context.cacheDir, getFileName(context, uri))
                            FileOutputStream(file).use { outputStream ->
                                inputStream.copyTo(outputStream)
                                val requestBody =
                                    file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                                val multipartBody = MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", file.name, requestBody)
                                    .build()

                                when (val result = networkRepository.uploadAudio(multipartBody)) {
                                    is Result.Success -> {
                                        Log.d(
                                            "UploadViewModel",
                                            "File upload successful: ${result.data}"
                                        )
                                        onResult(true, result.data.data.audioId)
                                    }
                                    else -> {
                                        Log.d(
                                            "UploadViewModel",
                                            "File upload failed: ${result.message}"
                                        )
                                        onResult(false, null)
                                    }
                                }
                            }
                            file.delete()
                        }
                    } ?: run {
                    Log.d("UploadViewModel", "Failed to open file descriptor")
                    onResult(false, null)
                }
            } catch (e: Exception) {
                Log.d("UploadViewModel", "Error uploading file: ${e.message}")
                onResult(false, null)
            }
        }
    }

    fun updateAudioInfo(audioId: String, description: String?, tags: List<String>?) {
        val tagsString = tags?.joinToString(separator = ",") ?: ""
        viewModelScope.launch {
            networkRepository.updateAudio(
                updateAudioBody = AudioUpdateRequestModel(
                    audioId = audioId,
                    description = description,
                    tags = tagsString
                )
            )
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

            else -> {
                Log.e("UploadViewModel", "Failed to fetch tag list")
            }
        }
    }
}
