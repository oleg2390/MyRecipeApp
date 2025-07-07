package com.example.myrecipeapp.ui.recipes.recipe

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myrecipeapp.R
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val portions: Int = 1,
)

class RecipeViewModel(
    application: Application,
    private val appPreferences: AppPreferences,

    ) : AndroidViewModel(application) {
    private val _uiState = MutableLiveData<RecipeUiState>()
    val uiState: LiveData<RecipeUiState> = _uiState

    init {
        Log.i("!!!", "ViewModel создан")
        _uiState.value?.copy(isFavorites = false)
    }

    fun loadRecipe(id: Int) {
        //TODO("load from network")

        val recipe = STUB.getRecipeById(id)
        val isFavorites = appPreferences.getFavorites().contains(id.toString())
        val current = _uiState.value ?: RecipeUiState()
        _uiState.value = current.copy(
            recipe = recipe,
            isFavorites = isFavorites,
            portions = current.portions
        )
    }

    fun onFavoritesClicked() {

        val current = _uiState.value ?: return
        val recipeId = current.recipe?.id.toString()
        val favorites = appPreferences.getFavorites().toMutableSet()
        val isCurrentlyFavorite = if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
            Toast.makeText(
                getApplication(),
                R.string.remove_favorite,
                Toast.LENGTH_SHORT
            ).show()
            false
        } else {
            favorites.add(recipeId)
            Toast.makeText(
                getApplication(),
                R.string.add_favorite,
                Toast.LENGTH_SHORT
            ).show()
            true
        }

        appPreferences.saveFavorites(favorites)
        _uiState.value = current.copy(isFavorites = isCurrentlyFavorite)
    }
}