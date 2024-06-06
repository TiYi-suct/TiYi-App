package com.tiyi.tiyi_app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    var username by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    Box(
        modifier = Modifier.background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "登录",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = {
                    Text("用户名")
                },
                leadingIcon = {
                    Icon(Icons.Outlined.AccountCircle, contentDescription = null)
                },
                trailingIcon = {

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text("密码")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* 处理登录逻辑 */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "登录")
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoginBlockPreview() {
    TiYiAppTheme {
        Surface {
            LoginBlock { }
        }
    }
}