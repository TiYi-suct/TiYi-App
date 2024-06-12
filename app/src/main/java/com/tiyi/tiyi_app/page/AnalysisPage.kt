package com.tiyi.tiyi_app.page

import android.app.Activity
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Paid
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.ResultActivity
import com.tiyi.tiyi_app.pojo.TransferAnalysisRequest
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import com.tiyi.tiyi_app.viewModel.AnalysisViewModel
import kotlinx.coroutines.launch

@Composable
fun AnalysisPage(
    modifier: Modifier = Modifier
) {
    val analysisViewModel: AnalysisViewModel = viewModel()
    val analysisItems by analysisViewModel.analysisItems.collectAsState()
    val transpositionSteps by analysisViewModel.transpositionSteps.collectAsState()
    val mfccFactor by analysisViewModel.mfccFactor.collectAsState()
    val sliceId by analysisViewModel.id.collectAsState()
    val sliceName by analysisViewModel.sliceName.collectAsState()
    val analysisCost by analysisViewModel.analysisCost.collectAsState()
    val error by analysisViewModel.error.collectAsState()
    val activity = LocalContext.current as Activity

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    fun showSnackBar(message: String) = coroutineScope.launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(message)
    }

    LaunchedEffect(error) {
        error?.let {
            showSnackBar(it)
            analysisViewModel.clearError()
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AnalysisAppBar(sliceName,
                onBackPressed = { activity.finish() })
        },
        bottomBar = {
            AnalysisPlayBottomBar(analysisCost,
                onAnalysisClick = {
                    val transferAnalysisRequest = TransferAnalysisRequest(
                        audioId = sliceId,
                        analysisItems = analysisItems,
                        transpositionSteps = transpositionSteps,
                        mfccFactor = mfccFactor
                    )

                    val intent = Intent(activity, ResultActivity::class.java).apply {
                        putExtra("analysisRequest", transferAnalysisRequest)
                    }
                    activity.startActivity(intent)
                })
        },
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier.padding(paddingValues)
        ) {
            items(analysisItems.keys.toList()) { analysisItem ->
                val checked = analysisItems[analysisItem]!!
                AnalysisItem(
                    analysisName = analysisItem.title,
                    analysisDescription = analysisItem.description,
                    checked = checked,
                    price = analysisItem.price,
                    onCheckedChange = {
                        analysisViewModel.updateAnalysisItemSelection(
                            analysisItem,
                            !checked
                        )
                    },
                    extendedControl = { modifier ->
                        DrawerOfAnalysisId(
                            analysisId = analysisItem.id,
                            transpositionSteps = transpositionSteps,
                            onTranspositionStepsChange = {
                                analysisViewModel.updateTranspositionSteps(
                                    it
                                )
                            },
                            mfccFactor = mfccFactor,
                            onMfccFactorChange = { analysisViewModel.updateMfccFactor(it) },
                            modifier = modifier
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun StartAnalysisButton(
    coinCost: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textStyle = MaterialTheme.typography.titleLarge
    val iconModifier = Modifier.size(textStyle.fontSize.value.dp)
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Outlined.Paid,
            contentDescription = "金币",
            modifier = iconModifier
        )
        Text(
            text = "$coinCost",
            style = textStyle
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.Check,
            contentDescription = "对勾",
            modifier = iconModifier
        )
    }
}

@Composable
fun DrawerOfAnalysisId(
    analysisId: Int,
    transpositionSteps: Int,
    onTranspositionStepsChange: (Int) -> Unit,
    mfccFactor: Int,
    onMfccFactorChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    when (analysisId) {
        2 -> {
            TranspositionStepsDrawer(
                transpositionSteps = transpositionSteps,
                onTranspositionStepsChange = onTranspositionStepsChange,
                modifier = modifier
            )
        }

        5 -> {
            MfccFactorDrawer(
                mfccFactor = mfccFactor,
                onMfccFactorChange = onMfccFactorChange,
                modifier = modifier
            )
        }

        else -> {

        }
    }
}

@Composable
fun MfccFactorDrawer(
    mfccFactor: Int,
    onMfccFactorChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            "MFCC 系数",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "$mfccFactor",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
            Slider(
                value = mfccFactor.toFloat(),
                onValueChange = { onMfccFactorChange(it.toInt()) },
                valueRange = 0f..20f,
                steps = 20,
                modifier = Modifier.weight(9f)
            )
        }
    }
}

@Composable
fun TranspositionStepsDrawer(
    transpositionSteps: Int,
    onTranspositionStepsChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            "音高变化（半音）",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(8.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "$transpositionSteps",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .weight(1f)
            )
            Slider(
                value = transpositionSteps.toFloat(),
                onValueChange = { onTranspositionStepsChange(it.toInt()) },
                valueRange = -6f..6f,
                steps = 12,
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    activeTickColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    inactiveTickColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
                modifier = Modifier.weight(9f)
            )
        }
    }
}

@Composable
fun AnalysisPlayBottomBar(
    analysisCost: Int,
    modifier: Modifier = Modifier,
    onAnalysisClick: () -> Unit = {},
) {
    var playProgress by remember { mutableFloatStateOf(0f) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp,
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(16.dp)
                .padding(bottom = 16.dp)
        ) {
            Slider(
                value = playProgress,
                onValueChange = { playProgress = it },
                valueRange = 0f..100f,
            )
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { /* do something */ },
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.PlayArrow,
                        contentDescription = "播放",
                        modifier = Modifier.size(64.dp)
                    )
                }
                StartAnalysisButton(
                    coinCost = analysisCost,
                    onClick = onAnalysisClick,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

    }
}

@OptIn(ExperimentalFoundationApi::class)
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
    var informationDialogVisible by remember { mutableStateOf(false) }
    if (informationDialogVisible) {
        AnalysisItemDetailDialog(
            analysisName = analysisName,
            analysisDescription = analysisDescription,
            onDismissRequest = { informationDialogVisible = false }
        )
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp,
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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    headlineColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    supportingColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    trailingIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                modifier = Modifier
                    .combinedClickable(
                        onClick = { onCheckedChange(!checked) },
                        onLongClick = { informationDialogVisible = true }
                    )
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

@Composable
fun AnalysisItemDetailDialog(
    analysisName: String,
    analysisDescription: String,
    onDismissRequest: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Outlined.Info, contentDescription = "信息")
        },
        title = {
            Text(text = analysisName)
        },
        text = {
            Text(text = analysisDescription)
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("好")
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisAppBar(
    sliceName: String,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit = {},
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
        )
    }
}

@Composable
@Preview(name = "PlayBottomBar - Light")
@Preview(
    name = "PlayBottomBar - Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
fun AnalysisPlayBottomBarPreview() {
    TiYiAppTheme {
        AnalysisPlayBottomBar(10)
    }
}

@Composable
@Preview(name = "TranspositionStepsDrawer - Light", showBackground = true)
fun TranspositionStepsDrawerPreview() {
    var sliderState by remember { mutableIntStateOf(2) }
    TiYiAppTheme {
        TranspositionStepsDrawer(
            transpositionSteps = sliderState,
            onTranspositionStepsChange = { sliderState = it }
        )
    }
}

@Composable
@Preview(name = "MfccFactorDrawer - Light", showBackground = true)
fun MfccFactorDrawerPreview() {
    var factor by remember { mutableIntStateOf(2) }
    TiYiAppTheme {
        MfccFactorDrawer(
            mfccFactor = factor,
            onMfccFactorChange = { factor = it }
        )
    }
}

@Composable
@Preview(name = "StartAnalysisButton - Light", showBackground = true)
fun StartAnalysisButtonPreview() {
    TiYiAppTheme {
        StartAnalysisButton(
            coinCost = 10,
            onClick = { /* do something */ }
        )
    }
}

@Composable
@Preview(name = "AnalysisItemDetailDialog - Light")
@Preview(
    name = "AnalysisItemDetailDialog - Dark",
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
fun AnalysisItemDetailDialogPreview() {
    TiYiAppTheme {
        AnalysisItemDetailDialog(
            analysisName = "BPM",
            analysisDescription = "Beats Per Minutes 每分钟的节拍数",
            onDismissRequest = { /* do something */ }
        )
    }
}
