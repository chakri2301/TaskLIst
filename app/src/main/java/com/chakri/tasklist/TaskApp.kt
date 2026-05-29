package com.chakri.tasklist

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                onTaskOpenedClicked = { task ->
                    appViewModel.setCurrentTask(task)
                    navController.navigate(AppScreens.ViewTask.name)
                },
                tasks = uiState.taskList.filter { appViewModel.isTaskVisible(it) },
                modifier = modifier.padding()
            )
        }
        composable(AppScreens.ViewTask.name) {
            ViewTaskScreen(
                uiState.currentTask,
                onCancelClicked = {
                    navController.navigateUp()
                },
                onSaveClicked = {
                    appViewModel.updateCompletion(it)
                    navController.navigateUp()
                }
            )
        }
        composable(AppScreens.CreateTask.name) {
            NewTaskScreen()
        }
    }
}