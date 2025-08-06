package com.example.myrecipeapp.ui.recipes.list_recipes

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.RecipesRepository
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import kotlinx.coroutines.launch
import java.io.IOException

data class RecipeListUiState(
    val recipes: List<Recipe> = emptyList(),
    val categoryName: String? = null,
    val categoryImage: Drawable? = null,
)

class RecipeListViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableLiveData<RecipeListUiState>()
    val state: LiveData<RecipeListUiState> = _state
    private val context = application
    private val repository = RecipesRepository()

    fun loadRecipeList(category: Category) {
        viewModelScope.launch {
            val recipes = repository.getRecipesByCategoryId(category.id)
            val drawable = try {
                val inputStream = context.assets.open(category.imageUrl)
                Drawable.createFromStream(inputStream, null)
            } catch (e: IOException) {
                Log.e("imageError", "Error image recipe")
                null
            }

            _state.value = RecipeListUiState(
                recipes = recipes ?: emptyList(),
                categoryName = category.title,
                categoryImage = drawable
            )
        }
    }
}

