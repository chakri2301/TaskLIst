package com.chakri.tasklist.data

import com.chakri.tasklist.model.Task
import com.chakri.tasklist.model.TaskName
import com.chakri.tasklist.network.TaskNetworkApi
import retrofit2.await

enum class TransactionState {
    Success,
    Error
}

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun updateTask(idx:Int,task:Task): TransactionState
    suspend fun deleteTask(idx:Int, task:Task): TransactionState
    suspend fun createTask(task: Task): TransactionState
}


class NetworkTaskRepository(val api: TaskNetworkApi) : TaskRepository {
    override suspend fun getAllTasks(): List<Task> {
        return api.getAllTasks()
    }

    override suspend fun updateTask(idx:Int, task:Task): TransactionState {
        api.updateTask(task)
        return TransactionState.Success
    }

    override suspend fun deleteTask(idx:Int,task:Task): TransactionState {
        api.delete(task.name)
        return TransactionState.Success
    }

    override suspend fun createTask(task: Task): TransactionState {
        api.addTask(task)
        return TransactionState.Success
    }
}

class FakeTaskRepository() : TaskRepository {
    var tasklist = mutableListOf(
        Task(
            name = "final",
            description = "complete",
            percentComplete = 70,
            deadline = 1000
        )
    )

    override suspend fun getAllTasks(): List<Task> {
        return tasklist.toList()
    }

    override suspend fun updateTask(idx:Int,task:Task ): TransactionState {
        if (tasklist[idx].name == task.name) {
            tasklist[idx] = Task(
                name = task.name,
                description = task.description,
                percentComplete = task.percentComplete,
                deadline = task.deadline,
            )
            return TransactionState.Success
        } else {
            return TransactionState.Error
        }
    }

    override suspend fun deleteTask(idx:Int, task:Task): TransactionState {
        tasklist.removeAt(idx)
        return TransactionState.Success
    }

    override suspend fun createTask(task: Task): TransactionState {
        var present = false
        if(task.name == "") return TransactionState.Error
        for(i in tasklist) {
            if(i.name == task.name){
                present = true
            }
        }
        if(!present){
            tasklist.add(task)
            return TransactionState.Success
        }else{
            return TransactionState.Error
        }
    }
}