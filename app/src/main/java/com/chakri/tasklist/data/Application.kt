package com.chakri.tasklist.data

import com.chakri.tasklist.network.TaskNetworkApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val networkTaskRepository: TaskRepository
}
class DefaultAppContainer() : AppContainer{
    private val BASEURL = "http://192.168.1.118:8080/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASEURL).build()
    private val retrofitService: TaskNetworkApi by lazy{
        retrofit.create(TaskNetworkApi::class.java)
    }
    override val networkTaskRepository: TaskRepository by lazy{
        NetworkTaskRepository(retrofitService)
    }
}


