package com.chakri.tasklist

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
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

enum class AppScreens {
    Home,
    CreateTask,
    ViewTask
}

@Composable
fun TaskApp(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {
    val uiState by appViewModel.uiState.collectAsState()
    NavHost(
        startDestination = AppScreens.Home.name,
        navController = navController
    ) {
        composable(AppScreens.Home.name) {
            HomeScreen(
                searchString = uiState.searchString,
                onSearchStringChanged = { appViewModel.setSearchString(it) },
                onCreateClicked = { navController.navigate(AppScreens.CreateTask.name) },
                onTaskOpenedClicked = { task ->
                    appViewModel.setCurrentTask(task)
                    navController.navigate(AppScreens.ViewTask.name)
                },
                tasks = uiState.taskList,
                modifier = modifier.padding()
            )
        }
        composable(AppScreens.ViewTask.name) {
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
                    }
                )
            }
        }
        composable(AppScreens.CreateTask.name) {
            NewTaskScreen(
                onCancelClicked = { navController.navigateUp() },
                onCreateClicked = { task: Task, updateGuideText: (String) -> Unit ->
                    val status = appViewModel.addTask(task)
                    if (status == TransactionState.Success) {
                        navController.navigateUp()
                    } else {
                        updateGuideText("Error: Either Name is empty (or) Name Exits")
                    }
                }
            )
        }
    }
}