package com.example.myrecipeapp.data

import android.app.Application
import android.util.Log
import androidx.room.Room

class MyApp : Application() {

    companion object {
        lateinit var database: AppDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()
        try {
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "categories_db"
            )
                .fallbackToDestructiveMigration()
                .build()
        }catch (e: Exception) {
            Log.e("!!!", "bd error", e)
            throw RuntimeException("Не удалось создать базу данных, e")
        }
    }
}