package com.example.myrecipeapp.ui.recipes.recipe

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myrecipeapp.data.AppPreferences

class RecipeViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val appPreferences = AppPreferences(application.applicationContext)
        return RecipeViewModel(application, appPreferences) as T
    }
}