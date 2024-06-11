package com.tiyi.tiyi_app.page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.viewModel.UploadViewModel

@Preview(name = "NIGHT MODE", showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun UploadPage(modifier: Modifier = Modifier) {
    FileUploadUI()
}

@Composable
fun FileUploadUI() {
    val viewModel: UploadViewModel = viewModel()
    val context = LocalContext.current

    val selectedFileItems = viewModel.selectedFileItems // 已选择文件列表
    val uploading = viewModel.uploading // 是否正在上传
    val uploadSuccess = viewModel.uploadSuccess // 上传成功列表
    val audioDescriptions = viewModel.audioDescriptions // 音频描述列表


    val filePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val fileName = getFileName(context, uri)
                    viewModel.updateSelectedFileItems(selectedFileItems + (fileName to uri))
                }
            }
        }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(16.dp)
                    .background(Color.Gray.copy(alpha = 0.1f))
                    .drawBehind {
                        val stroke = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                floatArrayOf(10.dp.toPx(), 10.dp.toPx()), 0f
                            )
                        )
                        drawRoundRect(color = Color.Gray, style = stroke)
                    }
                    .clickable {
                        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "audio/*"
                        }
                        filePickerLauncher.launch(intent)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "选择文件", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "已选择",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 16.dp)
            )

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(top = 30.dp, bottom = 72.dp)
                ) {
                    items(selectedFileItems) { item ->
                        AnalysisListItem(
                            itemText = item.first,
                            uri = item.second,
                            isUploading = uploading,
                            uploadSuccess = uploadSuccess[item.second] ?: false,
                            onDelete = { uri ->
                                viewModel.updateSelectedFileItems(selectedFileItems.filterNot { it.second == uri })
                                viewModel.updateAudioDescription(uri, "")
                            },
                            description = audioDescriptions[item.second] ?: "",
                            onDescriptionChange = { newDescription ->
                                viewModel.updateAudioDescription(item.second, newDescription)
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        Button(
            onClick = {
                selectedFileItems.forEach { (_, uri) ->
                    viewModel.updateUploading(true)
                    viewModel.uploadFile(uri) {
                        viewModel.updateUploading(false)
                        viewModel.updateUploadSuccess(uri, it)
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "上传文件")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisListItem(
    itemText: String,
    uri: Uri,
    isUploading: Boolean,
    uploadSuccess: Boolean,
    onDelete: (Uri) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemText.first().toString(),
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = itemText,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface
            )
            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp))
            } else if (uploadSuccess) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Uploaded", tint = Color.Green)
            } else {
                IconButton(onClick = { onDelete(uri) }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("描述") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// 获取文件名函数
@SuppressLint("Range")
fun getFileName(context: Context, uri: Uri): String {
    var fileName: String? = null
    val cursor: Cursor? = context.contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        }
    }
    return fileName ?: uri.lastPathSegment ?: "Unknown file"
}