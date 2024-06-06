package com.tiyi.tiyi_app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme

@Composable
fun LoginScreen(onLoginClick: () -> Unit, modifier: Modifier) {
    Scaffold(modifier = modifier) { innerPadding ->
        // 使用 innerPadding 作为内边距
        Button(onClick = { onLoginClick() }, modifier = Modifier.padding(innerPadding)) {
            Text(text = "登录")
        }
    }
}

data class LoginInfo(
    val username: String,
    val password: String,
)

@Composable
fun LoginBlock(onLoginClick: (result: LoginInfo) -> Unit) {

}

@Composable
@Preview(showBackground = true)
fun LoginBlockPreview() {
    TiYiAppTheme {
        Surface {
            LoginBlock {  }
        }
    }
}