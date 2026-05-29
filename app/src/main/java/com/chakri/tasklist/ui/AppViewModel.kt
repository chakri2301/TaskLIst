package com.chakri.tasklist.ui

import androidx.lifecycle.ViewModel
import com.chakri.tasklist.model.Task
import com.chakri.tasklist.model.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Instant


class AppViewModel: ViewModel() {
    private var _uiState = MutableStateFlow(UIState(searchString = ""))
    var uiState: StateFlow<UIState> = _uiState.asStateFlow()
    init{
        createTask("Ktor", "Create a ktor server1", Clock.System.now()+5.days, 20)
        createTask("Ktor", "Create a ktor server2", Clock.System.now()+5.days, 20)
        createTask("Ktor1", "Create a ktor server", Clock.System.now()+5.days, 20)
        createTask("Rooms", "Persistence in app", Clock.System.now()+5.days, 50)

    }
    fun updateCompletion(finalPercent:Int){
        _uiState.update {
            it.copy(
                currentTask = _uiState.value.currentTask.copy(percentComplete = finalPercent),
                taskList = _uiState.value.taskList.map {task->
                    if(task.name.equals(_uiState.value.currentTask.name)){newTask}else{task}
                }
            )
        }
    }
    fun setCurrentTask(task: Task){
        _uiState.update {
            it.copy(
                currentTask = task
            )
        }
    }
    fun createTask(name:String, description:String, deadline: Instant?, percent: Int){
        if(_uiState.value.taskList.find{it.name == name} == null || _uiState.value.taskList.isEmpty()) {
            val newTask = Task(name, description, deadline, percent)
            _uiState.update {
                it.copy(
                    taskList = uiState.value.taskList + newTask
                )
            }
        }
    }
    fun setSearchString(search:String){
        _uiState.update {
            it.copy(
                searchString = search
            )
        }
    }
    fun isTaskVisible(task:Task):Boolean{
        return task.name.contains(uiState.value.searchString, ignoreCase = true)
    }
}