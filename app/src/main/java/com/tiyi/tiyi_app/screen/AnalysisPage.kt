package com.tiyi.tiyi_app.screen

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview(name = "AnalysisPage", showSystemUi = true)
@Composable
fun AnalysisPage(modifier: Modifier = Modifier) {
    FileUploadUI()
}

@Composable
fun FileUploadUI() {
    // Temp variable
    val items = listOf("Crazy Car", "Forgive", "Happy", "Sad", "Sleepy", "开心游乐场", "可爱动物园", "Happy", "Sad", "Sleepy", "Happy", "Sad", "Sleepy", "Happy", "Sad", "Sleepy", "Happy", "Sad", "Sleepy", "Happy", "Sad", "Sleepy", "Happy", "S")

    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val filePickerLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                selectedFileUri = result.data?.data
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
                                floatArrayOf(
                                    10.dp.toPx(),
                                    10.dp.toPx()
                                ), 0f
                            )
                        )
                        drawRoundRect(
                            color = Color.Gray,
                            style = stroke
                        )
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

            selectedFileUri?.let {
                Text(text = "Selected file: $it", color = Color.Black)
            }

            Text(text = "已上载", modifier = Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp))

            Box(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(top = 30.dp,bottom = 72.dp)  // 确保底部有足够的空间给按钮
                ) {
                    items(items) { item ->
                        ListItem(itemText = item)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Top gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(MaterialTheme.colorScheme.background,MaterialTheme.colorScheme.background.copy(alpha = 0.7f), Color.Transparent)
                            )
                        )
                )

                // Bottom gradient overlay
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent,MaterialTheme.colorScheme.background.copy(alpha = 0.5f), MaterialTheme.colorScheme.background)
                            )
                        )
                )
            }

        }

        Button(
            onClick = { /* Handle analysis start */ },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Text(text = "开始分析")
        }
    }
}

@Composable
fun ListItem(itemText: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp)),

    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = itemText.first().toString(), color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp, textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = itemText, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
        Icon(imageVector = Icons.Outlined.Check, contentDescription = "Uploaded", tint = MaterialTheme.colorScheme.primary)
    }
}