package com.example.myrecipeapp.ui.recipes.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.model.Recipe
import kotlinx.coroutines.launch

data class FavoriteUiState(
    val recipes: List<Recipe> = emptyList(),
    val isEmpty: Boolean = true,
)

class FavoritesViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _state = MutableLiveData<FavoriteUiState>()
    val state: LiveData<FavoriteUiState> = _state
    var toastMessage = SingleLiveEvent<Int>()

    init {
        loadFavorite()
    }

    private fun loadFavorite() {

        viewModelScope.launch {
            val favoriteRecipes = recipesRepository.getFavoriteRecipe()

            val newStateFavorite = FavoriteUiState(
                recipes = favoriteRecipes,
                isEmpty = favoriteRecipes.isEmpty()
            )

            Log.i("!!!", "newStateFavorite - $newStateFavorite")
            _state.postValue(newStateFavorite)
        }
    }
}