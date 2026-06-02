package com.chakri.tasklist.data

import android.util.Log
import com.chakri.tasklist.dataApi.TaskDatabaseApi
import com.chakri.tasklist.dataApi.TaskNetworkApi
import com.chakri.tasklist.model.SortBy
import com.chakri.tasklist.model.Task
import com.chakri.tasklist.model.toTaskEntity

enum class TransactionState {
    Success, Error
}

interface TaskRepository {
    suspend fun getAllTasks(): List<Task>
    suspend fun updateTask(task: Task): TransactionState
    suspend fun deleteTask(task: Task): TransactionState
    suspend fun createTask(task: Task): TransactionState
}


class NetworkTaskRepository(val api: TaskNetworkApi) : TaskRepository {
    private var isNetworkAvaliable = false
    suspend fun checkConnectivity(): Boolean {
        try {
            val taskList = getAllTasks()
            isNetworkAvaliable = true
        } catch (e: Exception) {
            e.printStackTrace()
            isNetworkAvaliable = false
        }
        return isNetworkAvaliable
    }

    override suspend fun getAllTasks(): List<Task> {
        return api.getAllTasks()
    }

    override suspend fun updateTask(task: Task): TransactionState {
        try {
            api.updateTask(task)
            return TransactionState.Success
        } catch (e: Exception) {
            Log.i("chakri", "Fix this")
            e.printStackTrace()
            return TransactionState.Error
        }
    }

    override suspend fun deleteTask(task: Task): TransactionState {
        try {
            api.delete(task.name)
            return TransactionState.Success
        } catch (e: Exception) {
            Log.i("chakri", "Fix this")
            e.printStackTrace()
            return TransactionState.Error
        }
    }

    override suspend fun createTask(task: Task): TransactionState {
        try {
            api.addTask(task)
            return TransactionState.Success
        } catch (e: Exception) {
            Log.i("chakri", "Fix this")
            e.printStackTrace()
            return TransactionState.Error
        }
    }
}

class DBTaskRepository(val api: TaskDatabaseApi) : TaskRepository {
    override suspend fun getAllTasks(): List<Task> {
        return api.getAllTasks().map { it.toTask() }
    }
    suspend fun getFilteredTask(searchString: String): List<Task>{
        return api.filteredTasksByName(searchString).map { it.toTask() }
    }

    suspend fun clearDb() {
        api.clearDb()
    }

    override suspend fun updateTask(task: Task): TransactionState {
        try {
            api.updateTask(task.toTaskEntity())
        } catch (e: Exception) {
            Log.i("chakri", "Fix this")
            e.printStackTrace()
            return TransactionState.Error
        }
        return TransactionState.Success
    }

    override suspend fun deleteTask(task: Task): TransactionState {
        try {
            api.deleteTask(task.toTaskEntity())
            return TransactionState.Success
        } catch (e: Exception) {
            Log.i("chakri", "Fix this")
            e.printStackTrace()
            return TransactionState.Error
        }

    }

    override suspend fun createTask(task: Task): TransactionState {
        try {
            api.insertTask(task.toTaskEntity())
            return TransactionState.Success
        } catch (e: Exception) {
            Log.i("chakri", "Fix this")
            e.printStackTrace()
            return TransactionState.Error
        }
    }
}

class FakeTaskRepository() : TaskRepository {
    var tasklist = mutableListOf(
        Task(
            name = "final", description = "complete", percentComplete = 70, deadline = 1000
        )
    )

    override suspend fun getAllTasks(): List<Task> {
        return tasklist.toList()
    }

    override suspend fun updateTask(task: Task): TransactionState {
        var exists = false
        for (i in 0..(tasklist.size)) {
            if (tasklist[i].name == task.name) {
                exists = true
                tasklist[i] = task
            }
        }
        if (exists) {
            return TransactionState.Success
        } else {
            return TransactionState.Error
        }
    }

    override suspend fun deleteTask(task: Task): TransactionState {
        tasklist.remove(task)
        return TransactionState.Success
    }

    override suspend fun createTask(task: Task): TransactionState {
        var present = false
        if (task.name == "") return TransactionState.Error
        for (i in tasklist) {
            if (i.name == task.name) {
                present = true
            }
        }
        if (!present) {
            tasklist.add(task)
            return TransactionState.Success
        } else {
            return TransactionState.Error
        }
    }
}