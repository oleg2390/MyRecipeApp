package com.example.myrecipeapp.ui.recipes.recipe

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myrecipeapp.R
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Recipe
import java.io.IOException

data class RecipeUiState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val portions: Int = 1,
    val recipeImage: Drawable? = null,
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

        val drawable = try {
            val inputStream =
                recipe?.imageUrl?.let { getApplication<Application>().assets.open(it) }
            Drawable.createFromStream(inputStream, null)
        } catch (e: IOException) {
            val errorMsg =
                getApplication<Application>().applicationContext.getString(R.string.image_upload_error_from_assets)
            Log.e("image", errorMsg, e)
            null
        }

        val newState = current.copy(
            recipe = recipe,
            isFavorites = isFavorites,
            portions = current.portions,
            recipeImage = drawable,
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

    fun onPortionsChanged(newPortion: Int) {
        val current = uiState.value ?: return
        _uiState.value = current.copy(portions = newPortion)
    }
}