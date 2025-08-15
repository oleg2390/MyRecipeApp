package com.example.myrecipeapp.ui.recipes.list_recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import kotlinx.coroutines.launch

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

        viewModelScope.launch {
            val result = repository.getRecipesByCategoryId(category.id)
            _state.postValue(
                RecipeListUiState(
                    recipes = result ?: emptyList(),
                    categoryName = category.title,
                    categoryImage = category.imageUrl
                )
            )
        }
    }
}

