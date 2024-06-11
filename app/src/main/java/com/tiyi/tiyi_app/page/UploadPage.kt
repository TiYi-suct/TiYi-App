package com.tiyi.tiyi_app.page

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.viewModel.UploadViewModel

@Preview(name = "Upload Page", showSystemUi = true)
@Composable
fun UploadPage(modifier: Modifier = Modifier) {
    FileUploadUI()
}

@Composable
fun FileUploadUI() {
    val viewModel: UploadViewModel = viewModel()
    val context = LocalContext.current

    val selectedFileItems = viewModel.selectedFileItems // 已选择文件列表

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
        if (selectedFileItems.isEmpty()) {
            // 未选择文件时展示添加文件按钮
            IconButton(
                onClick = {
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        type = "audio/*"
                    }
                    filePickerLauncher.launch(intent)
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                colors = IconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Column {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add file",
                        modifier = Modifier.size(200.dp),
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "已选择",
                        modifier = Modifier
                            .weight(1f),
                        style = MaterialTheme.typography.titleLarge,
                    )
                    IconButton(onClick = {
                        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                            type = "audio/*"
                        }
                        filePickerLauncher.launch(intent)
                    }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add file")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(top = 30.dp, bottom = 72.dp)
                    ) {
                        items(selectedFileItems) { item ->
                            AnalysisListItem(
                                itemText = item.first,
                                uri = item.second
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
                        viewModel.uploadFile(uri) { success, audioId ->
                            viewModel.updateUploading(false) // 更新上传成功
                            viewModel.updateUploadSuccess(uri, success)
                            if (success && audioId != null) {
                                val description = viewModel.audioDescriptions[uri] ?: ""
                                val selectedTags = viewModel.selectedTags[uri] ?: emptyList()
                                viewModel.updateAudioInfo(audioId, description, selectedTags)
                            }
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisListItem(
    itemText: String,
    uri: Uri
) {
    val viewModel: UploadViewModel = viewModel()
    val isUploading = viewModel.uploading
    val uploadSuccess = viewModel.uploadSuccess[uri] ?: false
    val description = viewModel.audioDescriptions[uri] ?: ""
    val tags by viewModel.tagList.collectAsState()
    val selectedTags = remember { mutableStateListOf<String>() }

    // 初始化selectedTags
    LaunchedEffect(uri) {
        selectedTags.addAll(viewModel.selectedTags[uri] ?: emptyList())
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(16.dp)
    ) {
        LazyRow {
            items(tags) { tag ->
                TagItemForUpload(
                    tag = tag,
                    onSelectedChange = { isSelected ->
                        if (isSelected) {
                            selectedTags.add(tag)
                        } else {
                            selectedTags.remove(tag)
                        }
                        viewModel.updateSelectedTags(uri, selectedTags)
                    },
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
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
                IconButton(onClick = {
                    viewModel.updateSelectedFileItems(viewModel.selectedFileItems.filterNot { it.second == uri })
                    viewModel.updateAudioDescription(uri, "")
                    viewModel.updateSelectedTags(uri, emptyList())
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = description,
            onValueChange = { newDescription ->
                viewModel.updateAudioDescription(uri, newDescription)
            },
            label = { Text("描述") },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// 新增的TagItemForUpload组件，用于展示和选择标签
@Composable
fun TagItemForUpload(tag: String, onSelectedChange: (Boolean) -> Unit, modifier: Modifier) {
    var selected by remember { mutableStateOf(false) }
    FilterChip(
        onClick = {
            selected = !selected
            onSelectedChange(selected)
        },
        label = {
            Text(
                tag,
                style = MaterialTheme.typography.bodySmall
            )
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                )
            }
        } else {
            null
        },
        modifier = modifier
            .animateContentSize()
    )
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

