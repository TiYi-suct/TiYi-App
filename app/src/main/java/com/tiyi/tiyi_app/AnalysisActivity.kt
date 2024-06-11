package com.tiyi.tiyi_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.tiyi.tiyi_app.page.AnalysisPage
import com.tiyi.tiyi_app.ui.theme.TiYiAppTheme
import com.tiyi.tiyi_app.viewModel.AnalysisViewModel

class AnalysisActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val sliceName = intent.getStringExtra("title") ?: ""
        val id = intent.getStringExtra("id") ?: ""
        val analysisViewModel = ViewModelProvider(this)[AnalysisViewModel::class.java]
        analysisViewModel.updateAnalysisTarget(sliceName, id)
        setContent {
            TiYiAppTheme {
                AnalysisPage()
            }
        }
    }
}
