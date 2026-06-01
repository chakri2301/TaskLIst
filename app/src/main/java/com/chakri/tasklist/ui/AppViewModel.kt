package com.chakri.tasklist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.chakri.tasklist.AppScreens
import com.chakri.tasklist.TaskDataApplication
import com.chakri.tasklist.data.DBTaskRepository
import com.chakri.tasklist.data.NetworkTaskRepository
import com.chakri.tasklist.data.TransactionState
import com.chakri.tasklist.model.Task
import com.chakri.tasklist.model.UIState
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AppViewModel(
    val networkTaskRepository: NetworkTaskRepository,
    val dbTaskRepository: DBTaskRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow(UIState(searchString = "", errorString = null, currentScreen = AppScreens.Home))
    fun updateCurrentScreen(screen: AppScreens){
        _uiState.update {
            it.copy(
                currentScreen = screen
            )
        }
    }
    var uiState: StateFlow<UIState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            val tasklist: List<Task> = if (networkTaskRepository.checkConnectivity()) {
                dbTaskRepository.clearDb()
                networkTaskRepository.getAllTasks()
            }else{
                dbTaskRepository.getAllTasks()
            }
            _uiState.update {
                it.copy(
                    taskList = tasklist
                )
            }
        }
    }

    private fun updateList() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    taskList = dbTaskRepository.getAllTasks()
                )
            }
        }
    }

    suspend fun addTask(task: Task): TransactionState {
        var status = TransactionState.Success
        return viewModelScope.async {
            status = async{ networkTaskRepository.createTask(task)}.await()
            if(status == TransactionState.Success){
                dbTaskRepository.createTask(task)
                updateList()
            }
            return@async status
        }.await()
    }

    fun updateTask(task: Task): TransactionState {
        var status = TransactionState.Success
        viewModelScope.launch {
            status = networkTaskRepository.updateTask(task)
            if(status == TransactionState.Success){
                status = dbTaskRepository.updateTask(task)
                updateList()
            }
        }
        return status
    }

    fun deleteTask(task: Task): TransactionState {
        var status = TransactionState.Success
        viewModelScope.launch {
            status = networkTaskRepository.deleteTask(task)
            if (status == TransactionState.Success) {
                status = dbTaskRepository.deleteTask(task)
                updateList()
            }
        }
        return status
    }

    fun setCurrentTask(index: Int) {
        _uiState.update {
            it.copy(
                currentTask = index
            )
        }
    }

    fun setSearchString(search: String) {
        _uiState.update {
            it.copy(
                searchString = search
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val appApplication = (this[APPLICATION_KEY] as TaskDataApplication)
                val dataTaskRepository = appApplication.appContainer.dataTaskRepository
                val networkTaskRepository = appApplication.appContainer.networkTaskRepository
                return@initializer AppViewModel(
                    networkTaskRepository = networkTaskRepository,
                    dbTaskRepository = dataTaskRepository
                )
            }
        }
    }
}