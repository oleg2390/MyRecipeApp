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

class RecipeListViewModel(
    private val recipesRepository: RecipesRepository
) : ViewModel() {

    private val _state = MutableLiveData<RecipeListUiState>()
    val state: LiveData<RecipeListUiState> = _state
    var toastMessage = SingleLiveEvent<Int>()

    fun loadRecipeList(category: Category) {

        viewModelScope.launch {
            val cashedRecipes = recipesRepository.getRecipeByCategoriesIdFromCash()
            if (cashedRecipes.isNotEmpty()) {
                _state.postValue(
                    RecipeListUiState(
                        recipes = cashedRecipes,
                        categoryName = category.title,
                        categoryImage = category.imageUrl
                    )
                )
            }

            val result = recipesRepository.fetchAndSaveRecipesByCategory(category.id)
            result.onSuccess { recipes ->
                if (recipes.isNotEmpty()) {
                    _state.postValue(
                        RecipeListUiState(
                            recipes = recipes,
                            categoryName = category.title,
                            categoryImage = category.imageUrl
                        )
                    )
                }
            }
        }
    }
}

