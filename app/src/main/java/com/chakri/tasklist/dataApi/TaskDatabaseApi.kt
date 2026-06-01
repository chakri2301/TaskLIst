package com.chakri.tasklist.dataApi

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Update
import com.chakri.tasklist.model.TaskEntity

@Dao
interface TaskDatabaseApi {
    @Insert
    suspend fun insertTask(taskEntity: TaskEntity)
    @Query("SELECT * FROM task")
    suspend fun getAllTasks(): List<TaskEntity>
    @Delete
    suspend fun deleteTask(task: TaskEntity)
    @Update
    suspend fun updateTask(task: TaskEntity)
    @Query("SELECT * FROM task WHERE name = :name")
    suspend fun getTaskByName(name: String): List<TaskEntity>
    @Query("DELETE FROM task")
    suspend fun clearDb()
}

@Database(entities = [TaskEntity::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao() : TaskDatabaseApi
    companion object {
        var Instance: TaskDatabase? = null
        fun getDatabase(context: Context): TaskDatabase {
            return Instance ?: synchronized(this) {
                return Room.databaseBuilder(
                    context = context,
                    klass = TaskDatabase::class.java,
                    name = "taskDatabase"
                ).fallbackToDestructiveMigration(true)
                    .build()
                    .also { Instance = it }
            }
        }
    }
}