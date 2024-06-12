package com.tiyi.tiyi_app.page

import android.content.Intent
import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.AnalysisActivity
import com.tiyi.tiyi_app.pojo.AudioInfo
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import com.tiyi.tiyi_app.viewModel.RecentViewModel
import kotlinx.coroutines.launch

@Composable
fun NewTagDialog(
    onDismiss: () -> Unit, addTag: (tagName: String) -> Unit
) {
    var tagName by remember { mutableStateOf("") }
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp,
            ),
        ) {
            Text(
                text = "新建标签",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            OutlinedTextField(
                value = tagName,
                onValueChange = { tagName = it },
                label = {
                    Text("标签名")
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.End)
            ) {
                OutlinedButton(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(MaterialTheme.colorScheme.error)
                    )
                ) {
                    Text("取消")
                }
                Button(
                    onClick = {
                        addTag(tagName)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "确定",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun NewTagDialogPreview() {
    TiYiAppTheme {
        NewTagDialog({}, {})
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EditTagDialog(
    audioInfo: AudioInfo,
    availableTagList: List<String>,
    onDismiss: () -> Unit,
    onEdit: (List<String>) -> Unit
) {
    var selectedTags by remember { mutableStateOf(audioInfo.tags) }
    Dialog(
        onDismissRequest = { onDismiss() },
    ) {
        Card(
            colors = CardDefaults.cardColors().copy(
                containerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
        ) {
            Text(
                text = "编辑标签",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(16.dp)
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                for (tag in selectedTags) {
                    AssistChip(
                        onClick = {
                            selectedTags = selectedTags - tag
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "Done icon",
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            trailingIconContentColor = MaterialTheme.colorScheme.error
                        ),
                        label = {
                            Text(
                                tag,
                                style = MaterialTheme.typography.bodySmall
                            )
                        })

                }
            }
            LazyColumn(
                modifier = Modifier.heightIn(max = 240.dp)
            ) {
                itemsIndexed(availableTagList) { index, tag ->
                    val selected = selectedTags.contains(tag)
                    fun toggle() {
                        selectedTags = if (selected) {
                            selectedTags - tag
                        } else {
                            selectedTags + tag
                        }
                    }
                    ListItem(
                        headlineContent = {
                            Text(tag)
                        },
                        trailingContent = {
                            Checkbox(
                                checked = selected,
                                onCheckedChange = {
                                    toggle()
                                }
                            )
                        },
                        tonalElevation = 8.dp,
                        modifier = Modifier.clickable {
                            toggle()
                        }
                    )
                    if (index < availableTagList.lastIndex) {
                        HorizontalDivider(modifier = Modifier.height(1.dp))
                    }
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp)
            ) {
                OutlinedButton(
                    onClick = { onDismiss() },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        brush = SolidColor(MaterialTheme.colorScheme.error)
                    )
                ) {
                    Text("取消")
                }
                Button(
                    onClick = {
                        onEdit(selectedTags)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "确定",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
@Preview(name = "EditTagDialog")
@Preview(name = "EditTagDialog-Night", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun EditTagDialogPreview() {
    TiYiAppTheme {
        EditTagDialog(
            audioInfo = AudioInfo(
                "0",
                "Title",
                listOf("流行", "摇滚")
            ),
            availableTagList = listOf(
                "流行",
                "摇滚",
                "古典",
                "电子",
                "爵士",
                "民谣",
                "说唱",
                "轻音乐"
            ),
            onDismiss = {},
            onEdit = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun RecentPage(
    refresh: Boolean,
    acquiredRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val recentViewModel: RecentViewModel = viewModel()
    val tags by recentViewModel.tagList.collectAsState()
    val songs by recentViewModel.recentList.collectAsState()
    val loading by recentViewModel.loading.collectAsState()
    val error by recentViewModel.error.collectAsState()
    var selectedTags by remember { mutableStateOf(emptyList<String>()) }
    var query by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

//    LaunchedEffect(songs) {
//        listState.animateScrollToItem(0)
//    }
    LaunchedEffect(refresh) {
        if (refresh) {
            acquiredRefresh()
            recentViewModel.refreshAudioList()
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    fun showSnackBar(message: String) = coroutineScope.launch {
        snackbarHostState.currentSnackbarData?.dismiss()
        snackbarHostState.showSnackbar(message)
    }

    LaunchedEffect(error) {
        error?.let {
            showSnackBar(it)
            recentViewModel.clearError()
        }
    }

    var newTagDialogVisible by remember { mutableStateOf(false) }
    if (newTagDialogVisible) {
        NewTagDialog(
            onDismiss = { newTagDialogVisible = false },
            addTag = { recentViewModel.addTag(it) }
        )
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = {
                    query = it
                    recentViewModel.searchMusic(query)
                },
                onSearch = { recentViewModel.searchMusic(query) },
                active = false,
                onActiveChange = {},
                trailingIcon = {
                    IconButton(
                        onClick = { recentViewModel.searchMusic(query) }
                    ) {
                        Icon(Icons.Outlined.Search, contentDescription = "搜索")
                    }
                },
                placeholder = {
                    Text(
                        "搜索音频",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                tonalElevation = 8.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .fillMaxWidth()
            ) { }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = loading,
                enter = expandHorizontally(),
                exit = shrinkHorizontally()
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        modifier = modifier.fillMaxSize()
            .imePadding()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .fillMaxSize()
        ) {
            val visibleTransitionState = remember {
                MutableTransitionState(false).apply {
                    targetState = true
                }
            }
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                items(tags, key = { it }) { tag ->
                    AnimatedVisibility(
                        visibleState = visibleTransitionState,
                        enter = fadeIn() + expandHorizontally(),
                        exit = fadeOut() + shrinkHorizontally()
                    ) {
                        TagItem(
                            tag = tag, onSelectedChange = {
                                selectedTags = if (it) {
                                    selectedTags + tag
                                } else {
                                    selectedTags - tag
                                }
                                recentViewModel.updateSelectedTagList(selectedTags)
                            },
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                        )
                    }
                }
                item {
                    AssistChip(
                        colors = AssistChipDefaults.assistChipColors(
                            labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        onClick = {
                            newTagDialogVisible = true
                        },
                        leadingIcon = {
                            Icon(Icons.Filled.PlusOne, contentDescription = "添加标签")
                        },
                        label = {
                            Text("添加")
                        },
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .animateItemPlacement()
                    )
                }
            }
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxHeight()
            ) {
                items(songs, key = {it.id}) { music ->
                    // 后端返回之前在前端先过滤
                    if (selectedTags.isNotEmpty() && !music.tags.any { selectedTags.contains(it) })
                        return@items
                    MusicItem(
                        music,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement()
                    )
                }
            }
        }
    }
}

@Composable
fun MusicItem(audioInfo: AudioInfo, modifier: Modifier = Modifier) {
    var isExpended by rememberSaveable { mutableStateOf(false) }
    var editTagDialogVisible by remember { mutableStateOf(false) }
    val recentViewModel: RecentViewModel = viewModel()
    val context = LocalContext.current
    val tagList by recentViewModel.tagList.collectAsState()
    if (editTagDialogVisible) {
        EditTagDialog(
            audioInfo = audioInfo,
            availableTagList = tagList,
            onDismiss = { editTagDialogVisible = false },
            onEdit = { tags ->
                recentViewModel.editTagFor(audioInfo, tags)
            }
        )
    }
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 2.dp
        ),
        onClick = { isExpended = !isExpended },
        modifier = modifier
            .padding(8.dp)
            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = audioInfo.title.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    audioInfo.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    audioInfo.tagList,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        AnimatedVisibility(
            visible = isExpended,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier.align(Alignment.End)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
            ) {
                Button(
                    onClick = {
                        recentViewModel.deleteMusic(audioInfo)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        "删除",
                        color = MaterialTheme.colorScheme.onError
                    )
                }
                Button(
                    onClick = {
                        editTagDialogVisible = true
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text(
                        "标签",
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Button(
                    onClick = {
                        Intent(context, AnalysisActivity::class.java).apply {
                            putExtra("id", audioInfo.id)
                            putExtra("title", audioInfo.title)
                            context.startActivity(this)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        "分析",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}

@Composable
fun TagItem(tag: String, onSelectedChange: (Boolean) -> Unit, modifier: Modifier) {
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