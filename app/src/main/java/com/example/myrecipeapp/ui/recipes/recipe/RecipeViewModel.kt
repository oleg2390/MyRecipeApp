package com.example.myrecipeapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipeapp.R
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Recipe
import com.example.myrecipeapp.utils.Event

data class RecipeUiState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val portions: Int = 1,
)

class RecipeViewModel(
    private val appPreferences: AppPreferences,

    ) : ViewModel() {
    private val _uiState = MutableLiveData<RecipeUiState>()
    val uiState: LiveData<RecipeUiState> = _uiState
    private val _toastMessage = MutableLiveData<Event<Int>>()
    val toastMessage : LiveData<Event<Int>> = _toastMessage

    init {
        Log.i("!!!", "ViewModel создан")
        _uiState.value?.copy(isFavorites = false)
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

        val current = _uiState.value ?: return
        val recipeId = current.recipe?.id.toString()
        val favorites = appPreferences.getFavorites().toMutableSet()
        val (isFavoriteNow, toastResId) = if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
            false to R.string.remove_favorite
        } else {
            favorites.add(recipeId)
            true to R.string.add_favorite
        }

        appPreferences.saveFavorites(favorites)
        _uiState.value = current.copy(isFavorites = isFavoriteNow)
        _toastMessage.value = Event(toastResId)
    }
}