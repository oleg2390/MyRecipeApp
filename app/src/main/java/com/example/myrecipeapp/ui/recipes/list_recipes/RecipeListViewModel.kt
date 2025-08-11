package com.example.myrecipeapp.ui.recipes.list_recipes

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myrecipeapp.R
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
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
    var toastMessage = SingleLiveEvent<Int>()

    fun loadRecipeList(category: Category) {

        repository.getRecipesByCategoryId(category.id) { recipes ->
            if (recipes == null) {
                toastMessage.postValue(R.string.errorToast)
                return@getRecipesByCategoryId
            }

            val drawable = try {
                val inputStream = context.assets.open(category.imageUrl)
                Drawable.createFromStream(inputStream, null)
            } catch (e: IOException) {
                Log.e("imageError", "Error image recipe")
                null
            }

            _state.postValue(
                RecipeListUiState(
                    recipes = recipes,
                    categoryName = category.title,
                    categoryImage = drawable
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.shutdown()
    }
}

