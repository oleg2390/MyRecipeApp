package com.example.myrecipeapp

import android.app.Application
import com.example.myrecipeapp.di.AppContainer

class RecipeApplication : Application() {

    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()

        appContainer = AppContainer(this)
    }
}