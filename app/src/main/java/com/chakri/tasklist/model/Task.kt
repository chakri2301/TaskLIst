package com.chakri.tasklist.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
enum class TransactionType{
    CREATE,
    UPDATE,
    DELETE
}
enum class EntityStatus{
    C,// create
    U,// Update
    D,// Delete
    N // None
}

@Serializable
data class Task(
    val name: String,
    val description: String,
    @SerialName("percent")
    val percentComplete: Short,
    val deadline: Long?
)
fun Task.toTaskEntity(): TaskEntity{
    return TaskEntity(
        name = name,
        description = description,
        percentComplete = percentComplete,
        deadline = deadline
    )
}

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey val name:String,
    val description: String,
    @ColumnInfo(name="percent") val percentComplete: Short,
    val deadline: Long?
){
    fun toTask():Task{
        return Task(
            name = name,
            description = description,
            percentComplete = percentComplete,
            deadline = deadline
        )
    }
}
