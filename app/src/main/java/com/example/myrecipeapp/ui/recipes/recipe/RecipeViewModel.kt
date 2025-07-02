package com.example.myrecipeapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipeapp.model.Recipe

data class RecipeUiState(
    val recipe: Recipe? = null,
    val isFavorites: Boolean = false,
    val portions: Int = 1,
    val errorMassage: String? = null,
    val imageDrawable: Drawable? = null,
)

class RecipeViewModel : ViewModel() {
    private val _uiState = MutableLiveData<RecipeUiState>()
    val uiState: LiveData<RecipeUiState> = _uiState
}