package com.tiyi.tiyi_app

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.tiyi.tiyi_app.pojo.AnalysisRequest
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import com.tiyi.tiyi_app.viewModel.ResultViewModel

class ResultActivity : ComponentActivity() {
    private lateinit var analysisRequest: AnalysisRequest
    private lateinit var resultViewModel: ResultViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analysisRequest = intent.getSerializableExtra("analysisRequest", AnalysisRequest::class.java) ?: return
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

    private fun requestAnalysisResults() {
        if (analysisRequest.analysisItems.filter { it.key.id == 3 } .values.contains(true)) {
            Log.d("analysis", "requestAnalysisResults: analysis mel spec")
            resultViewModel.analysisMelSpec(analysisRequest.audioId)
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