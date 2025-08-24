package com.example.myrecipeapp.ui.recipes.recipe

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.R
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.model.Recipe
import kotlinx.coroutines.launch

data class RecipeUiState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val portions: Int = 1,
    val recipeImage: String? = null,
)

class RecipeViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableLiveData<RecipeUiState>()
    val uiState: LiveData<RecipeUiState> = _uiState
    private val repository = RecipesRepository()

    var toastMessage = SingleLiveEvent<Int>()

    init {
        Log.i("!!!", "ViewModel создан")
    }

    fun loadRecipe(id: Int) {

        viewModelScope.launch {
            val recipe = repository.getRecipeByIdFromDb(id)
            val current = uiState.value ?: RecipeUiState()

            if (recipe == null) return@launch

            val newState = current.copy(
                recipe = recipe,
                isFavorites = recipe.isFavorite,
                portions = current.portions,
                recipeImage = recipe.imageUrl,
            )

            _uiState.postValue(newState)
        }
    }

    fun onFavoritesClicked() {

        val current = uiState.value ?: return
        val recipe = current.recipe ?: return

        viewModelScope.launch {
            val isNowFavorites = !recipe.isFavorite
            repository.updateFavorite(recipe.id, isNowFavorites)
            _uiState.postValue(current.copy(recipe = recipe.copy(isFavorite = isNowFavorites)))

            val toastResId = if (isNowFavorites) {
                R.string.add_favorite
            } else R.string.remove_favorite

            toastMessage.postValue(toastResId)
        }
    }

    fun onPortionsChanged(newPortion: Int) {
        val current = uiState.value ?: return
        _uiState.value = current.copy(portions = newPortion)
    }
}