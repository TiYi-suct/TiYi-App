package com.tiyi.tiyi_app

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.tiyi.tiyi_app.pojo.TransferAnalysisRequest
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import com.tiyi.tiyi_app.viewModel.ResultViewModel

class ResultActivity : ComponentActivity() {
    private lateinit var transferAnalysisRequest: TransferAnalysisRequest
    private lateinit var resultViewModel: ResultViewModel

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transferAnalysisRequest = intent.getSerializableExtra("analysisRequest", TransferAnalysisRequest::class.java) ?: return
        resultViewModel = ViewModelProvider(this)[ResultViewModel::class.java]
        requestAnalysisResults()
        enableEdgeToEdge()
        setContent {
            TiYiAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun requestAnalysisResults(): ? {
        if (transferAnalysisRequest.analysisItems.filter { it.key.id == 3 } .values.contains(true)) {
            Log.d("analysis", "requestAnalysisResults: analysis mel spec")
            resultViewModel.analysisMelSpec(transferAnalysisRequest.audioId)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TiYiAppTheme {
        Greeting("Android")
    }
}