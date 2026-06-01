package com.chakri.tasklist

import android.R
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chakri.tasklist.data.TransactionState
import com.chakri.tasklist.model.Task
import com.chakri.tasklist.ui.AppViewModel
import com.chakri.tasklist.ui.HomeScreen
import com.chakri.tasklist.ui.NewTaskScreen
import com.chakri.tasklist.ui.ViewTaskScreen

enum class AppScreens(val title: String) {
    Home(title = "Task List"),
    CreateTask(title = "Create a task"),
    ViewTask(title = "Task")
}

@Composable
fun TaskApp(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {
    val uiState by appViewModel.uiState.collectAsState()
    Scaffold(
        topBar = { TopAppBarComposable(uiState.currentScreen.title) },
        modifier = modifier
    ) { innerPadding ->
        NavHost(
            startDestination = AppScreens.Home.name,
            navController = navController
        ) {
            composable(AppScreens.Home.name) {
                appViewModel.updateCurrentScreen(AppScreens.Home)
                HomeScreen(
                    searchString = uiState.searchString,
                    onSearchStringChanged = { appViewModel.setSearchString(it) },
                    onCreateClicked = { navController.navigate(AppScreens.CreateTask.name) },
                    onTaskOpenedClicked = { task ->
                        appViewModel.setCurrentTask(task)
                        navController.navigate(AppScreens.ViewTask.name)
                    },
                    tasks = uiState.taskList,
                    modifier = modifier.padding(innerPadding)
                )
            }
            composable(AppScreens.ViewTask.name) {
                appViewModel.updateCurrentScreen(AppScreens.ViewTask)
                if (uiState.taskList.isEmpty()) {
                    Text("NO TASK SELECTED. Redirecting to home")
                    Button(
                        onClick = {
                            navController.navigate(AppScreens.Home.name)
                        }
                    ) {
                        Text("Return to home")
                    }
                } else {
                    ViewTaskScreen(
                        uiState.taskList[uiState.currentTask],
                        onCancelClicked = {
                            navController.navigateUp()
                        },
                        onSaveClicked = { percent ->
                            appViewModel.updateTask(
                                uiState.taskList[uiState.currentTask].copy(
                                    percentComplete = percent.toShort()
                                )
                            )
                            navController.navigateUp()
                        },
                        onDeleteClicked = {
                            appViewModel.deleteTask(uiState.taskList[uiState.currentTask])
                            appViewModel.setCurrentTask(0)
                            navController.navigateUp()
                        },
                        modifier = modifier.padding(innerPadding)
                    )
                }
            }
            composable(AppScreens.CreateTask.name) {
                appViewModel.updateCurrentScreen(AppScreens.Home)
                NewTaskScreen(
                    onCancelClicked = { navController.navigateUp() },
                    onCreateClicked = { task: Task, updateGuideText: (String) -> Unit ->
                        val state: TransactionState = appViewModel.addTask(task)
                        if (state == TransactionState.Error) {
                            updateGuideText("Error task name exists (or) Empty name (or) no network")
                        } else {
                            navController.navigateUp()
                        }
                    },
                    modifier = modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun TopAppBarComposable(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text= title,
            style = MaterialTheme.typography.displayMedium
        )
    }
}