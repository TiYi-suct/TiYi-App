package com.tiyi.tiyi_app.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.tiyi.tiyi_app.R
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme

@Composable
fun ResultPage(
    modifier: Modifier = Modifier
) {

}

@Composable
fun ResultItemNumber(
    resultName: String,
    number: Double,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "$resultName = ",
                style = MaterialTheme.typography.headlineLarge,
            )

            Text(
                text = number.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.End)
            )
        }
    }
}

@Composable
fun ResultItemDownload(
    resultName: String,
    onDownloadClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = resultName,
                style = MaterialTheme.typography.headlineLarge,
            )

            Button(
                onClick = { onDownloadClicked(true) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(Icons.Filled.ArrowDownward, contentDescription = "下载")
                Spacer(modifier = Modifier.padding(4.dp))
                Text(text = "下载结果")
            }
        }
    }
}

@Composable
fun ResultItemImage(
    resultName: String,
    imageRequest: ImageRequest,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        colors = CardDefaults.elevatedCardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
        ){
            Text(
                text = resultName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
            SubcomposeAsyncImage(
                model = imageRequest,
                loading = {
                    CircularProgressIndicator()
                },
                contentScale = ContentScale.Crop,
                contentDescription = "结果",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .sizeIn(maxHeight = 200.dp)
                ,
            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.End)
                    .padding(16.dp)
            ) {
                Icon(Icons.Outlined.Search, contentDescription = "放大镜")
                Text(text = "查看")
            }
        }

    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ResultAppBar(
        sliceName: String,
        modifier: Modifier = Modifier,
    ) {
        TopAppBar(
            title = {
                Text(
                    text = sliceName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                }
            },
            modifier = modifier
        )
    }

    @Composable
    @Preview(name = "Result AppBar - Light")
    @Preview(name = "Result AppBar - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun ResultAppBarPreview() {
        TiYiAppTheme {
            ResultAppBar(sliceName = "Slice Name")
        }
    }

    @Composable
    @Preview(name = "Result Item Number - Light")
    @Preview(name = "Result Item Number - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun ResultItemNumberPreview() {
        TiYiAppTheme {
            ResultItemNumber(resultName = "BPM", number = 128.73)
        }
    }

    @Composable
    @Preview(name = "Result Item Download - Light")
    @Preview(name = "Result Item Download - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun ResultItemDownloadPreview() {
        TiYiAppTheme {
            ResultItemDownload(
                resultName = "移调：+3",
                onDownloadClicked = {}
            )
        }
    }

    @Composable
    @Preview(name = "Result Item Image - Light")
    @Preview(name = "Result Item Image - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
    fun ResultItemImagePreview() {
        TiYiAppTheme {
            ResultItemImage(
                resultName = "MFCC：20",
                imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(R.drawable.login_background)
                    .build()
            )
        }
    }