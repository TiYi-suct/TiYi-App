package com.tiyi.tiyi_app.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tiyi.tiyi_app.screen.AnalysisPage
import com.tiyi.tiyi_app.screen.LoginScreen
import com.tiyi.tiyi_app.screen.ProfilePage
import com.tiyi.tiyi_app.screen.RecentPage

@Preview
@Composable
fun TiYiApp() {
    val isLogin = remember { mutableStateOf(true) }
    if (isLogin.value) {
        MainScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    } else {
        LoginScreen(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    val menuData = listOf(
        BottomItemData("最近文件", Icons.Outlined.CheckCircle, "RecentPage"),
        BottomItemData("分析", Icons.Outlined.Star, "AnalysisPage"),
        BottomItemData("我的", Icons.Outlined.AccountCircle, "ProfilePage"),
    )

    Scaffold(modifier = modifier.fillMaxSize(), bottomBar = {
        BottomNavigationBar(navController = navController, items = menuData)
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
            composable("AnalysisPage") { AnalysisPage(Modifier.fillMaxSize()) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, items: List<BottomItemData>) {
    val currentDestination by navController.currentBackStackEntryAsState()
    val currentRoute = currentDestination?.destination?.route
    NavigationBar(modifier = Modifier.fillMaxWidth()) {

        items.forEachIndexed { _, item ->
            NavigationBarItem(
                icon = {
                    Icon(imageVector = item.icon, contentDescription = item.label)
                },
                label = { Text(text = item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            restoreState = true
                        }
                    }
                })
        }
    }
}

data class BottomItemData(
    val label: String,
    val icon: ImageVector,
    val route: String,
)