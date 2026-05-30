package com.chakri.tasklist.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class Task(
    val name: String,
    val description: String,
    @SerialName("percent")
    val percentComplete: Short,
    val deadline: Long
)
@Serializable
data class TaskName(
    val name:String
)