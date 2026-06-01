package com.chakri.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chakri.tasklist.data.TransactionState
import com.chakri.tasklist.model.Task
import com.chakri.tasklist.ui.AppViewModel
import com.chakri.tasklist.ui.EditTaskScreen
import com.chakri.tasklist.ui.HomeScreen
import com.chakri.tasklist.ui.NewTaskScreen
import com.chakri.tasklist.ui.ViewTaskScreen

enum class AppScreens(val title: String) {
    Home(title = "Task List"),
    CreateTask(title = "Create a task"),
    ViewTask(title = "Task"),
    EditTask(title = "Edit Task")
}

@Composable
fun TaskApp(
    modifier: Modifier = Modifier,
    appViewModel: AppViewModel,
    navController: NavHostController = rememberNavController()
) {
    val uiState by appViewModel.uiState.collectAsState()
    Scaffold(
        topBar = { TopAppBarComposable(
            uiState.currentScreen.title,
            errorText = uiState.errorString ,
            onCloseClick = {appViewModel.syncServer()}
        ) },
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
                        onEditClicked = {navController.navigate(AppScreens.EditTask.name)},
                        modifier = modifier.padding(innerPadding)
                    )
                }
            }
            composable(AppScreens.CreateTask.name) {
                appViewModel.updateCurrentScreen(AppScreens.Home)
                NewTaskScreen(
                    onCancelClicked = { navController.navigateUp() },
                    onCreateClicked = { task: Task ->
                        val state: TransactionState = appViewModel.addTask(task)
                        if (state == TransactionState.Error) {
                            appViewModel.updateErrorText("Name is duplicate or empty")
                        } else {
                            navController.navigateUp()
                        }
                    },
                    modifier = modifier.padding(innerPadding)
                )
            }
            composable(AppScreens.EditTask.name){
                EditTaskScreen(
                    task = uiState.taskList[uiState.currentTask],
                    onCancelClicked = {
                        navController.navigateUp()
                    },
                    onChangeClicked = {
                        if(it.name == uiState.taskList[uiState.currentTask].name){
                            appViewModel.updateTask(it)
                        }else{
                            appViewModel.deleteTask(uiState.taskList[uiState.currentTask])
                            appViewModel.addTask(it)
                        }
                        navController.navigate(AppScreens.Home.name)
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
    errorText:String?,
    onCloseClick:()->Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium
            )
        }
        if(errorText != null){
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ){
                    Text(
                        text = errorText,
                        color = MaterialTheme.colorScheme.onError,
                        textAlign = TextAlign.Center,
                    )
                    IconButton(
                        onClick = onCloseClick
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.close),
                            contentDescription = "Close error"
                        )
                    }
                }
            }
        }
    }

}