package com.example.myrecipeapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val portions: Int = 1,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val appPreferences = AppPreferences(application.applicationContext)
    private val _uiState = MutableLiveData<RecipeUiState>()
    val uiState: LiveData<RecipeUiState> = _uiState

    init {
        Log.i("!!!", "ViewModel создан")
    }

    fun loadRecipe(id: Int) {
        //TODO("load from network")
        val recipe = STUB.getRecipeById(id)
        val isFavorites = appPreferences.getFavorites().contains(id.toString())
        val current = uiState.value ?: RecipeUiState()
        val newState = current.copy(
            recipe = recipe,
            isFavorites = isFavorites,
            portions = current.portions
        )

        _uiState.value = newState
    }

    fun onFavoritesClicked() {

        val current = uiState.value ?: return
        val recipeId = current.recipe?.id.toString()
        val favorites = appPreferences.getFavorites().toMutableSet()
        val isNowFavorite = if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
            false
        } else {
            favorites.add(recipeId)
            true
        }

        appPreferences.saveFavorites(favorites)
        _uiState.value = current.copy(isFavorites = isNowFavorite)
    }
}