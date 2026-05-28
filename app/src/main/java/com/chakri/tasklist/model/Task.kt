package com.chakri.tasklist.model

import java.time.Instant


data class Task(
    val name:String,
    val description:String,
    val deadline: Instant
)