package com.example.myrecipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myrecipeapp.data.AppPreferences

class RecipeViewModelFactory(
    private val appPreferences: AppPreferences
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RecipeViewModel(appPreferences) as T
    }
}