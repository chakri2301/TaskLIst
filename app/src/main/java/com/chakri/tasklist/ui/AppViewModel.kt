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
import com.chakri.tasklist.model.SortBy
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
    private var _uiState = MutableStateFlow(
        UIState(
            searchString = "",
            errorString = null,
            currentScreen = AppScreens.Home,
            netAvailable = false
        )
    )

    fun updateCurrentScreen(screen: AppScreens) {
        _uiState.update {
            it.copy(
                currentScreen = screen
            )
        }
    }

    var uiState: StateFlow<UIState> = _uiState.asStateFlow()
    fun updateErrorText(text: String?) {
        _uiState.update {
            it.copy(
                errorString = text
            )
        }
    }

    fun syncServer() {
        viewModelScope.launch {
            var tasklist: List<Task>
            val isNet: Boolean
            if (networkTaskRepository.checkConnectivity()) {
                dbTaskRepository.clearDb()
                tasklist = networkTaskRepository.getAllTasks()
                for (tk in tasklist) {
                    dbTaskRepository.createTask(tk)
                }
                isNet = true
            } else {
                tasklist = dbTaskRepository.getAllTasks()
                isNet = false
            }
            _uiState.update {
                it.copy(
                    taskList = tasklist,
                    netAvailable = isNet,
                    errorString = if (isNet) null else "Unable to connect Unavailable. Click to retry"
                )
            }
        }
    }

    init {
        syncServer()
    }

    private fun updateList() {
        viewModelScope.launch {
            if (_uiState.value.searchString == "" && _uiState.value.sortBy == SortBy.none) {
                _uiState.update {
                    it.copy(
                        taskList = dbTaskRepository.getAllTasks(),
                        loading = false
                    )
                }
            }else{
                val multiplier = if(_uiState.value.isAscSort) 1 else -1
                var taskList =  dbTaskRepository.getFilteredTask(_uiState.value.searchString)
                taskList = when(_uiState.value.sortBy){
                    SortBy.Name -> taskList.sortedWith { task1, task2 -> multiplier*task1.name.compareTo(task2.name)}
                    SortBy.Percent -> taskList.sortedBy {it.percentComplete*multiplier }
                    SortBy.Deadline -> taskList.sortedWith { task1, task2 -> multiplier*((task1.deadline?.toInt()?:Int.MIN_VALUE) -(task2.deadline?.toInt()?:Int.MIN_VALUE)) }
                    SortBy.none ->taskList
                }
                _uiState.update {
                    it.copy(
                        taskList =taskList,
                        loading = false
                    )
                }
            }
        }
    }

    private fun startLoad() {
        _uiState.update {
            it.copy(
                loading = true
            )
        }
    }
    suspend fun addTask(task: Task): TransactionState {
        var status = TransactionState.Success
        return viewModelScope.async {
            startLoad()
            status = async { networkTaskRepository.createTask(task) }.await()
            if (status == TransactionState.Success) {
                dbTaskRepository.createTask(task)
                updateList()
            }
            return@async status
        }.await()
    }

    fun updateTask(task: Task): TransactionState {
        var status = TransactionState.Success
        viewModelScope.launch {
            startLoad()
            status = networkTaskRepository.updateTask(task)
            if (status == TransactionState.Success) {
                status = dbTaskRepository.updateTask(task)
                updateList()
            }
        }
        return status
    }

    fun deleteTask(task: Task): TransactionState {
        var status = TransactionState.Success
        viewModelScope.launch {
            startLoad()
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
                searchString = search,
            )
        }
        updateList()
    }
    fun setSortBy(sortBy: SortBy){
        _uiState.update {
            it.copy(
                sortBy = sortBy
            )
        }
        updateList()
    }
    fun setSortOrder(isAscOrder:Boolean){
        _uiState.update {
            it.copy(
                isAscSort = isAscOrder
            )
        }
        updateList()
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