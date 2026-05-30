package com.chakri.tasklist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.chakri.tasklist.model.Task
import com.chakri.tasklist.ui.AppViewModel
import com.chakri.tasklist.ui.HomeScreen
import com.chakri.tasklist.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding()
                ) { innerPadding ->
                    TaskApp(
                        appViewModel = viewModel(factory = AppViewModel.Factory),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AppPreview() {
    AppTheme {
        HomeScreen(
            searchString = "Hi",
            onSearchStringChanged = {},
            onTaskOpenedClicked = {},
            onCreateClicked = {},
            tasks = listOf(Task("server", "with ktor", percentComplete = 10, deadline = 100))
        )
    }
}