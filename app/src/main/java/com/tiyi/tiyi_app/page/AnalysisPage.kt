package com.tiyi.tiyi_app.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme


@Suppress("FunctionName")
@Composable
fun AnalysisPage(modifier: Modifier) {

}

@Composable
fun AnalysisItem(
    analysisName: String,
    analysisDescription: String,
    checked: Boolean,
    price: Int,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    extendedControl: @Composable (
        modifier: Modifier
    ) -> Unit = {},
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .animateContentSize()
    ) {
        Column {
            ListItem(
                headlineContent = {
                    Text(
                        text = analysisName,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                supportingContent = {
                    Text(
                        text = analysisDescription,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                trailingContent = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Paid,
                            contentDescription = "Localized description"
                        )
                        Text(
                            text = "$price",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Checkbox(
                            checked,
                            onCheckedChange,
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
                modifier = Modifier
                    .clickable { onCheckedChange(!checked) }
            )
            AnimatedVisibility(
                visible = checked,
            ) {
                extendedControl(
                    Modifier.padding(16.dp)
                )
            }
            HorizontalDivider()
        }
    }
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

@Composable
@Preview(name = "ListItem - Light")
@Preview(name = "ListItem - Dark", uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
fun AnalysisItemPreview() {
    TiYiAppTheme {
        var checked by remember { mutableStateOf(true) }
        var sliderPosition by remember { mutableFloatStateOf(37f) }

        AnalysisItem(
            analysisName = "BPM",
            analysisDescription = "Beats Per Minutes 每分钟的节拍数",
            checked = checked,
            price = 10,
            onCheckedChange = { checked = !checked },
            extendedControl = { modifier ->
                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.5f
                        ),
                    ),
                    modifier = modifier
                )
            },
            modifier = Modifier.safeDrawingPadding()
        )
    }
}