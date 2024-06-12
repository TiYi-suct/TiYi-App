package com.tiyi.tiyi_app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.tiyi.tiyi_app.page.ResultPage
import com.tiyi.tiyi_app.pojo.TransferAnalysisRequest
import com.tiyi.tiyi_app.pojo.toAnalysisRequest
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
        resultViewModel.updateAnalysisRequest(transferAnalysisRequest.toAnalysisRequest())

        enableEdgeToEdge()
        setContent {
            TiYiAppTheme {
                ResultPage(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}