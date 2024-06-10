package com.tiyi.tiyi_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alipay.sdk.app.EnvUtils
import com.tiyi.tiyi_app.ui.MainScreen
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiYiAppTheme {
                MainScreen()
            }
        }
    }
}