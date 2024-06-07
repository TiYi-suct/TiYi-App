package com.tiyi.tiyi_app.screen

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
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
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tiyi.tiyi_app.data.MusicInfo
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import kotlin.random.Random

val fakeTags = listOf(
    "流行",
    "摇滚",
    "古典",
    "电子",
    "爵士",
    "民谣",
    "说唱",
    "轻音乐",
)

fun generateRandomString(length: Int): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
    return (1..length)
        .map { chars.random() }
        .joinToString("")
}

val fakeSongs = Array(100) {
    MusicInfo(
        it, generateRandomString(
            Random(it).nextInt(1, 10)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentPage(modifier: Modifier) {
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
                items(fakeTags) { tag ->
                    TagItem(tag = tag, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }
            LazyColumn {
                items(fakeSongs) {
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

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp,
            pressedElevation = 2.dp
        ),
        modifier = modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
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
            Text(musicInfo.title.repeat(3))
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
