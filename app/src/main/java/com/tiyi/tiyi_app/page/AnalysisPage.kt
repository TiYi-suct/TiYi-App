package com.tiyi.tiyi_app.page

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme


@Suppress("FunctionName")
@Composable
fun AnalysisPage(modifier: Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisAppBar(
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
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview(name = "AppBar - Light")
@Preview(name = "AppBar - Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun AnalysisAppBarPreview() {
    TiYiAppTheme {
        Column {
            AnalysisAppBar(sliceName = "Analysis Slice")
            AnalysisAppBar(sliceName = "Analysis Slice ".repeat(5))
        }
    }
}
