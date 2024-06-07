package com.tiyi.tiyi_app.screen

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecentPage(modifier: Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column {
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
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Recent")
                Text(text = "Recent")
                Text(text = "Recent")
            }
        }
    }
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
