package com.chakri.tasklist.dataApi

import com.chakri.tasklist.model.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface TaskNetworkApi {
    @GET("/tasks")
    suspend fun getAllTasks():List<Task>
    @PUT("/task")
    suspend fun updateTask(@Body task:Task)
    @POST("/task")
    suspend fun addTask(@Body task: Task)
    @DELETE("/task/delete/{name}")
    suspend fun delete(@Query("name")name:String)
}