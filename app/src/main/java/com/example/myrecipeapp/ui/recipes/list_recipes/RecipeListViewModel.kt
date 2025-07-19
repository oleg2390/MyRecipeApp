package com.example.myrecipeapp.ui.recipes.list_recipes

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.example.myrecipeapp.ARG_CATEGORY_ID
import com.example.myrecipeapp.ARG_CATEGORY_IMAGE_URL
import com.example.myrecipeapp.ARG_CATEGORY_NAME
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Recipe
import java.io.IOException

data class RecipeListUiState(
    val recipes: List<Recipe> = emptyList(),
    val categoryName: String? = null,
    val categoryImage: Drawable? = null,
)

class RecipeListViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle,

    ) : AndroidViewModel(application) {

    private val _state = MutableLiveData<RecipeListUiState>()
    val state: LiveData<RecipeListUiState> = _state
    private val context = application

    init {
        loadRecipeList()
    }

    private fun loadRecipeList() {

        val categoryId = savedStateHandle.get<Int>(ARG_CATEGORY_ID) ?: return
        val categoryName = savedStateHandle.get<String>(ARG_CATEGORY_NAME)
        val categoryImageUrl = savedStateHandle.get<String>(ARG_CATEGORY_IMAGE_URL)

        val recipes = STUB.getRecipesByCategoryId(categoryId)

        val drawable = try {
            categoryImageUrl?.let { fileName ->
                val inputStream = context.assets.open(fileName)
                Drawable.createFromStream(inputStream, null)
            }
        } catch (e: IOException) {
            Log.e("imageError", "Error image recipe")
            null
        }

        _state.value = RecipeListUiState(
            recipes = recipes,
            categoryName = categoryName,
            categoryImage = drawable
        )
    }
}

