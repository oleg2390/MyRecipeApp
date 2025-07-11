package com.example.myrecipeapp.ui.recipes.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Recipe

data class FavoriteUiState(
    val recipes: List<Recipe> = emptyList(),
    val isEmpty : Boolean = true,
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<FavoriteUiState>()
    val state: LiveData<FavoriteUiState> = _state
    private val appPreferences = AppPreferences(application.applicationContext)

    init {
        loadFavorite()
    }

    private fun loadFavorite() {

        val favoritesId: Set<Int> = appPreferences
            .getFavorites()
            .mapNotNull { it.toIntOrNull() }
            .toSet()

        val favoriteRecipes = STUB.getRecipesByIds(favoritesId)

        val currentFavorite = state.value ?: FavoriteUiState()
        val newStateFavorite = currentFavorite.copy(
            recipes = favoriteRecipes,
            isEmpty = favoriteRecipes.isEmpty()
        )

        _state.value = newStateFavorite
    }
}