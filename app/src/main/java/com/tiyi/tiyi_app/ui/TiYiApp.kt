package com.tiyi.tiyi_app.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tiyi.tiyi_app.screen.AnalysisPage
import com.tiyi.tiyi_app.screen.ProfilePage
import com.tiyi.tiyi_app.screen.RecentPage

@Preview
@Composable
fun TiYiApp() {
    val isLogin = remember { mutableStateOf(false) }

    if (isLogin.value) {
        MainScreen(
            onLoginClick = { isLogin.value = false },
            modifier = Modifier
                .fillMaxSize()
        )
    } else {
        LoginScreen(
            onLoginClick = { isLogin.value = true },
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp) // 设置固定内边距
        )
    }
}

@Composable
fun MainScreen(onLoginClick: () -> Unit, modifier: Modifier) {
    val navController = rememberNavController()

    var currentSelect by remember {
        mutableStateOf(0)
    }

    data class BottomItemData(
        val label: String,
        val icon: ImageVector
    )

    val menuData = listOf(
        BottomItemData("最近文件", Icons.Outlined.CheckCircle),
        BottomItemData("分析", Icons.Outlined.Star),
        BottomItemData("我的", Icons.Outlined.AccountCircle),
    )

    Scaffold(modifier = modifier.fillMaxSize(), bottomBar = {
        NavigationBar(modifier = Modifier.fillMaxWidth()) {
            menuData.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    },
                    label = { Text(text = item.label) },
                    selected = index == currentSelect,
                    onClick = {
                        navController.navigate(
                            when (index) {
                                0 -> "RecentPage"
                                1 -> "AnalysisPage"
                                2 -> "ProfilePage"
                                else -> "RecentPage"
                            },
                        ){
                            launchSingleTop = true
                        }
                    })
            }
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "RecentPage",
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable("RecentPage") { RecentPage(Modifier.fillMaxSize()) }
            composable("ProfilePage") { ProfilePage(Modifier.fillMaxSize()) }
            composable("AnalysisPage") { AnalysisPage(Modifier.fillMaxSize())}
        }
    }
}

@Composable
fun LoginScreen(onLoginClick: () -> Unit, modifier: Modifier) {
    Scaffold(modifier = modifier) { innerPadding ->
        // 使用 innerPadding 作为内边距
        Button(onClick = { onLoginClick() }, modifier = Modifier.padding(innerPadding)) {
            Text(text = "登录")
        }
    }
}