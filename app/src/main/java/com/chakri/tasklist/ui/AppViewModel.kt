package com.chakri.tasklist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.chakri.tasklist.TaskDataApplication
import com.chakri.tasklist.data.FakeTaskRepository
import com.chakri.tasklist.data.TaskRepository
import com.chakri.tasklist.data.TransactionState
import com.chakri.tasklist.model.Task
import com.chakri.tasklist.model.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AppViewModel(val taskRepository: TaskRepository) : ViewModel() {
    private var _uiState = MutableStateFlow(UIState(searchString = ""))
    var uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    taskList = taskRepository.getAllTasks()
                )
            }
        }
    }
    private fun updateList(){
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    taskList = taskRepository.getAllTasks()
                )
            }
        }
    }
    fun addTask(task:Task): TransactionState{
        var status= TransactionState.Success
        viewModelScope.launch {
            status = taskRepository.createTask(task)
            updateList()
        }
        return status
    }
    fun updateTask(task:Task): TransactionState{
        var status= TransactionState.Success
        viewModelScope.launch {
            status = taskRepository.updateTask(_uiState.value.currentTask,task)
            updateList()
        }
        return status
    }
    fun deleteTask(task:Task): TransactionState{
        var status= TransactionState.Success
        viewModelScope.launch {
            status = taskRepository.deleteTask(_uiState.value.currentTask,task)
            updateList()
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
                val taskRepository = appApplication.appContainer.networkTaskRepository
                AppViewModel(taskRepository)
            }
        }
    }
}