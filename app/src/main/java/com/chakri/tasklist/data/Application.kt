package com.chakri.tasklist.data

import android.content.Context
import com.chakri.tasklist.dataApi.TaskDatabase
import com.chakri.tasklist.dataApi.TaskDatabaseApi
import com.chakri.tasklist.dataApi.TaskNetworkApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val networkTaskRepository: NetworkTaskRepository
    val dataTaskRepository: DBTaskRepository

}
class DefaultAppContainer(context: Context) : AppContainer{
    //Network retrofit connection
    private val bASEURL = "http://192.168.1.118:8080/"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(bASEURL).build()
    private val retrofitService: TaskNetworkApi by lazy{
        retrofit.create(TaskNetworkApi::class.java)
    }
    override val networkTaskRepository: NetworkTaskRepository by lazy{
        NetworkTaskRepository(retrofitService)
    }
    //-----------------------------
    //SQLite database connection
    private val dbApi : TaskDatabaseApi by lazy{
        TaskDatabase.getDatabase(context).taskDao()
    }
    override val dataTaskRepository: DBTaskRepository by lazy {
        DBTaskRepository(dbApi)
    }
    //-----------------------------
}


