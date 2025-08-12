package com.example.myrecipeapp.ui.recipes.list_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipeapp.R
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe

data class RecipeListUiState(
    val recipes: List<Recipe> = emptyList(),
    val categoryName: String? = null,
    val categoryImage: String? = null,
)

class RecipeListViewModel() : ViewModel() {

    private val _state = MutableLiveData<RecipeListUiState>()
    val state: LiveData<RecipeListUiState> = _state
    private val repository = RecipesRepository()
    var toastMessage = SingleLiveEvent<Int>()

    fun loadRecipeList(category: Category) {

        repository.getRecipesByCategoryId(category.id) { recipes ->
            if (recipes == null) {
                toastMessage.postValue(R.string.errorToast)
                return@getRecipesByCategoryId
            }

            _state.postValue(
                RecipeListUiState(
                    recipes = recipes,
                    categoryName = category.title,
                    categoryImage = category.imageUrl
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.shutdown()
    }
}

