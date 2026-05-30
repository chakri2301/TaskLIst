package com.chakri.tasklist.network

import com.chakri.tasklist.model.Task
import com.chakri.tasklist.model.TaskName
import retrofit2.Call
import retrofit2.Response
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