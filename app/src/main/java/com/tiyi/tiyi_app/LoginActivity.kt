package com.tiyi.tiyi_app

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tiyi.tiyi_app.viewModel.LoginStatus
import com.tiyi.tiyi_app.viewModel.LoginViewModel
import com.tiyi.tiyi_app.page.LoginScreen
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private lateinit var loginViewModel: LoginViewModel

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        lifecycleScope.launch {
            loginViewModel.loginStatus.collect { status ->
                when (status) {
                    is LoginStatus.Error -> {
                        Toast.makeText(
                            this@LoginActivity,
                            "登录失败: ${status.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                    LoginStatus.Success -> {
                        Intent(this@LoginActivity, MainActivity::class.java).also {
                            startActivity(it)
                        }
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }
        }
        setContent {
            LoginActivityContent(
                onLoginClick = {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                    }
                },
                onRegisterClick = {
                    Intent(this, MainActivity::class.java).also {
                        startActivity(it)
                    }
                }
            )
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}

@Composable
fun LoginActivityContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {}
) {
    TiYiAppTheme {
        Surface {
            LoginScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
@Preview
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewLoginActivity() {
    LoginActivityContent()
}