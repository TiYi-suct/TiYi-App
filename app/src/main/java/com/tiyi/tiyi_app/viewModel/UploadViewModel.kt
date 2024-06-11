package com.tiyi.tiyi_app.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
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

class UploadViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val tiyiApplication = application as TiyiApplication
    private val networkRepository = tiyiApplication.networkRepository

    fun uploadFile(uri: Uri, onResult: (Boolean) -> Unit) {
        // 在 ViewModel 的协程作用域中启动一个新的协程
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            try {
                // 尝试通过内容解析器打开文件描述符
                context.contentResolver.openFileDescriptor(uri, "r", null)
                    ?.use { parcelFileDescriptor ->
                        // 使用文件输入流读取文件内容
                        FileInputStream(parcelFileDescriptor.fileDescriptor).use { inputStream ->
                            // 创建一个临时文件存储在缓存目录中
                            val file = File(context.cacheDir, getFileName(context, uri))
                            // 使用文件输出流将输入流内容写入临时文件
                            FileOutputStream(file).use { outputStream ->
                                inputStream.copyTo(outputStream)
                                // 将文件转换为请求体
                                val requestBody =
                                    file.asRequestBody("audio/mpeg".toMediaTypeOrNull())
                                // 构建 multipart 请求体
                                val multipartBody = MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", file.name, requestBody)
                                    .build()

                                // 通过网络仓库上传文件，并处理结果
                                when (val result = networkRepository.uploadAudio(multipartBody)) {
                                    is Result.Success -> {
                                        Log.d(
                                            "UploadViewModel",
                                            "File upload successful: ${result.data}"
                                        )
                                        // 上传成功，回调返回 true
                                        onResult(true)
                                    }

                                    else -> {
                                        Log.d(
                                            "UploadViewModel",
                                            "File upload failed: ${result.message}"
                                        )
                                        // 上传失败，回调返回 false
                                        onResult(false)
                                    }
                                }
                            }
                            file.delete() // 使用后删除文件
                        }
                    } ?: run {
                    // 如果文件描述符打开失败，记录日志并回调返回 false
                    Log.d("UploadViewModel", "Failed to open file descriptor")
                    onResult(false)
                }
            } catch (e: Exception) {
                // 捕获异常，记录日志并回调返回 false
                Log.d("UploadViewModel", "Error uploading file: ${e.message}")
                onResult(false)
            }
        }
    }

}