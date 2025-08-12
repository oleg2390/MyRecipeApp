package com.example.myrecipeapp.ui.recipes.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.model.Recipe
import kotlinx.coroutines.launch

data class FavoriteUiState(
    val recipes: List<Recipe> = emptyList(),
    val isEmpty: Boolean = true,
)

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<FavoriteUiState>()
    val state: LiveData<FavoriteUiState> = _state
    private val appPreferences = AppPreferences(application.applicationContext)
    private val repository = RecipesRepository()
    var toastMessage = SingleLiveEvent<Int>()

    init {
        loadFavorite()
    }

    private fun loadFavorite() {

        viewModelScope.launch {
            val favoritesId: Set<Int> = appPreferences
                .getFavorites()
                .mapNotNull { it.toIntOrNull() }
                .toSet()

            val favoriteRecipes = repository.getRecipesByIds(favoritesId)

            val newStateFavorite = if (favoriteRecipes != null) {
                FavoriteUiState(
                    recipes = favoriteRecipes,
                    isEmpty = favoriteRecipes.isEmpty()
                )
            } else {
                FavoriteUiState(
                    recipes = emptyList(),
                    isEmpty = true
                )
            }

            Log.i("!!!", "newStateFavorite - $newStateFavorite")
            _state.postValue(newStateFavorite)
        }
    }
}