package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.application.TiyiApplication
import com.tiyi.tiyi_app.page.getFileName
import com.tiyi.tiyi_app.pojo.Result
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.jvm.Throws

class AnalysisViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val tiyiApplication = application as TiyiApplication
    private val networkRepository = tiyiApplication.networkRepository

    fun uploadFile(uri: Uri) {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>().applicationContext
                val parcelFileDescriptor =
                    context.contentResolver.openFileDescriptor(uri, "r", null)
                parcelFileDescriptor?.let {
                    val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
                    val file = File(context.cacheDir, getFileName(context, uri))
                    val outputStream = FileOutputStream(file)
                    inputStream.copyTo(outputStream)

                    val requestBody = file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                    val multipartBody = MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", file.name, requestBody)
                        .build()

                    when (val result = networkRepository.uploadFile(multipartBody)) {
                        is Result.Success -> {
                            Log.d("AnalysisViewModel", "File upload successful: ${result.data}")
                        }

                        else -> {
                            Log.d("AnalysisViewModel", "File upload failed: ${result.message}")
                            throw (Exception("File upload failed"))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // TODO: 显示上传失败
            }
        }
    }

}