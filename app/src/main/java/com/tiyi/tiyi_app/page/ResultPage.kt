package com.tiyi.tiyi_app.page

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.tiyi.tiyi_app.R
import com.tiyi.tiyi_app.pojo.analysis.AnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.BpmAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.MelSpectrogramAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.MfccAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.SpectrogramAnalysisRequest
import com.tiyi.tiyi_app.pojo.analysis.TranspositionAnalysisRequest
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import com.tiyi.tiyi_app.viewModel.ResultViewModel

@Composable
fun ResultPage(
    modifier: Modifier = Modifier
) {
    val resultViewModel: ResultViewModel = viewModel()
    val analysisRequests by resultViewModel.analysisRequest.collectAsState()
    val sliceName by resultViewModel.sliceName.collectAsState()
    val error by resultViewModel.error.collectAsState()
    val activity = LocalContext.current as Activity

    Scaffold(
        topBar = {
            ResultAppBar(sliceName = sliceName,
                onBackPressed = { activity.finish() })
        },
        modifier = modifier
    ) { paddingValue ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValue)
                .padding(16.dp)
        ) {
            items(analysisRequests, key = { it.analysisId }) { analysisRequest ->
                CombinedResultItem(analysisRequest = analysisRequest)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun CombinedResultItem(
    analysisRequest: AnalysisRequest<*>,
) {
    val resultViewModel: ResultViewModel = viewModel()
    when (analysisRequest) {
        is BpmAnalysisRequest -> {
            val bpm by rememberSaveable { resultViewModel.take(analysisRequest) }
            ResultItemNumber(resultName = "BPM", number = bpm)
        }

        is TranspositionAnalysisRequest -> {
            val url by rememberSaveable { resultViewModel.take(analysisRequest) }
            ResultItemDownload(resultName = "移调", onDownloadClicked = { /*TODO*/ }, url = url)
        }

        is MelSpectrogramAnalysisRequest -> {
            val url by rememberSaveable { resultViewModel.take(analysisRequest) }
            ResultItemImage(
                resultName = "梅尔频谱图",
                url,
                imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build()
            )
        }

        is SpectrogramAnalysisRequest -> {
            val url by rememberSaveable { resultViewModel.take(analysisRequest) }
            ResultItemImage(
                resultName = "频谱图",
                url,
                imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build()
            )
        }

        is MfccAnalysisRequest -> {
            val url by rememberSaveable { resultViewModel.take(analysisRequest) }
            ResultItemImage(
                resultName = "MFCC",
                url,
                imageRequest = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .build()
            )
        }
    }
}

@Composable
fun ResultItemNumber(
    resultName: String,
    number: Float?,
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

            if (number == null) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.End)
                )
            } else {
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
}

@Composable
fun ResultItemDownload(
    resultName: String,
    onDownloadClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    url: String? = null,
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
                enabled = url != null,
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
    rawUrl: String?,
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
                .fillMaxWidth()
        ) {
            Text(
                text = resultName,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.padding(16.dp)
            )
            if (rawUrl == null) {
                LinearProgressIndicator()
            } else {
                SubcomposeAsyncImage(
                    model = imageRequest,
                    loading = {
                        LinearProgressIndicator()
                    },
                    contentScale = ContentScale.Crop,
                    contentDescription = "结果",
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .sizeIn(maxHeight = 200.dp)
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .align(Alignment.End)
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
    onBackPressed: () -> Unit,
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
            IconButton(onClick = onBackPressed) {
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
        ResultAppBar(onBackPressed = {}, sliceName = "Slice Name")
    }
}

@Composable
@Preview(name = "Result Item Number - Light")
@Preview(name = "Result Item Number - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun ResultItemNumberPreview() {
    TiYiAppTheme {
        ResultItemNumber(resultName = "BPM", number = 128.73f)
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
            null,
            imageRequest = ImageRequest.Builder(LocalContext.current)
                .data(R.drawable.login_background)
                .build()
        )
    }
}