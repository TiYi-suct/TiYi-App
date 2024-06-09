package com.tiyi.tiyi_app.screen

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tiyi.tiyi_app.data.MusicInfo
import com.tiyi.tiyi_app.model.RecentViewModel
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentPage(modifier: Modifier) {
    val recentViewModel: RecentViewModel = viewModel()
    val tags by recentViewModel.tagList.collectAsState()
    val songs by recentViewModel.recentList.collectAsState()

    var newTagDialogVisible by remember { mutableStateOf(false) }
    if (newTagDialogVisible) {
        NewTagDialog(
            onDismiss = { newTagDialogVisible = false },
            addTag = { recentViewModel.addTag(it) }
        )
    }
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Top
        ) {
            SearchBar(
                query = "",
                onQueryChange = { },
                onSearch = { },
                active = false,
                onActiveChange = {},
                leadingIcon = {
                    Icon(Icons.Outlined.Menu, contentDescription = "菜单")
                },
                trailingIcon = {
                    Icon(Icons.Outlined.Search, contentDescription = "搜索")
                },
                tonalElevation = 8.dp,
                shadowElevation = 4.dp,
                modifier = Modifier
                    .offset(y = (-16).dp)
                    .padding(horizontal = 16.dp, vertical = 0.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) { }
            LazyRow(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            ) {
                items(tags) { tag ->
                    TagItem(tag = tag, modifier = Modifier.padding(horizontal = 4.dp))
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
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }
            LazyColumn {
                items(songs) {
                    MusicItem(
                        it,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun MusicItem(musicInfo: MusicInfo, modifier: Modifier = Modifier) {
    var isExpended by rememberSaveable { mutableStateOf(false) }
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
                    text = musicInfo.title.first().toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.size(16.dp))
            Column {
                Text(
                    musicInfo.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    musicInfo.description,
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
                    onClick = {},
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
                    onClick = {},
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
                    onClick = {},
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
fun TagItem(tag: String, modifier: Modifier) {
    var selected by remember { mutableStateOf(false) }
    FilterChip(
        onClick = { selected = !selected },
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

@Composable
@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun RecentPagePreview() {
    TiYiAppTheme {
        Surface {
            RecentPage(modifier = Modifier)
        }
    }
}
