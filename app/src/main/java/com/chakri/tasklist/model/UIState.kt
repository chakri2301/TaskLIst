package com.chakri.tasklist.model

import com.chakri.tasklist.AppScreens

data class UIState(
    val currentTask : Int = 0,
    val taskList: List<Task> = listOf(),
    val searchString:String,
    val errorString: String?,
    val currentScreen : AppScreens,
    val netAvailable:Boolean
)