package com.chakri.tasklist

import android.app.Application
import com.chakri.tasklist.data.AppContainer
import com.chakri.tasklist.data.DefaultAppContainer

class TaskDataApplication: Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = DefaultAppContainer()
    }
}