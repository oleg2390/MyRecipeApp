package com.example.myrecipeapp.ui.recipes.favorite

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myrecipeapp.R
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.model.Recipe

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

        val favoritesId: Set<Int> = appPreferences
            .getFavorites()
            .mapNotNull { it.toIntOrNull() }
            .toSet()

        repository.getRecipesByIds(favoritesId) { favoriteRecipes ->
            if (favoriteRecipes == null) {
                toastMessage.postValue(R.string.errorToast)
                return@getRecipesByIds
            }

            val newStateFavorite = FavoriteUiState(
                recipes = favoriteRecipes,
                isEmpty = favoriteRecipes.isEmpty()
            )

            Log.i("!!!", "newStateFavorite - $newStateFavorite")
            _state.postValue(newStateFavorite)
        }
    }
}