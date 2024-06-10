package com.tiyi.tiyi_app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.tiyi.tiyi_app.page.AnalysisPage
import com.tiyi.tiyi_app.page.LoginScreen
import com.tiyi.tiyi_app.page.ProfilePage
import com.tiyi.tiyi_app.page.RecentPage
import com.tiyi.tiyi_app.viewModel.MainViewModel
import kotlinx.coroutines.launch

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
    val mainViewModel: MainViewModel = viewModel()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val error by mainViewModel.error.collectAsState()

    LaunchedEffect(error) {
        error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                mainViewModel.clearError()
            }
        }
    }

    val menuData = listOf(
        BottomItemData("最近文件", Icons.Outlined.CheckCircle, "RecentPage"),
        BottomItemData("分析", Icons.Outlined.Star, "AnalysisPage"),
        BottomItemData("我的", Icons.Outlined.AccountCircle, "ProfilePage"),
    )

    Scaffold(modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Column {
                val loading by mainViewModel.loading.collectAsState()
                if (loading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                BottomNavigationBar(navController = navController, items = menuData)
            }
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "RecentPage",
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            composable("RecentPage") { RecentPage(
                submitError = mainViewModel::submitError,
                Modifier.fillMaxSize()
            ) }
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