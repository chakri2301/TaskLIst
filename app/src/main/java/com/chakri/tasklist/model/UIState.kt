package com.chakri.tasklist.model

data class UIState(
    val currentTask : Int = 0,
    val taskList: List<Task> = listOf(),
    val searchString:String
)