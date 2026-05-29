package com.chakri.tasklist.model

import kotlin.time.Instant


data class Task(
    val name: String,
    val description: String,
    val deadline: Instant?,
    val percentComplete: Int
)