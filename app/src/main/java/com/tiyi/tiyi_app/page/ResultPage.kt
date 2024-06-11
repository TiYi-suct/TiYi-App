package com.tiyi.tiyi_app.page

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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