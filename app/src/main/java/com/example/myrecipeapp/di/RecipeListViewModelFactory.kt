package com.example.myrecipeapp.di

import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.ui.recipes.list_recipes.RecipeListViewModel

class RecipeListViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipeListViewModel> {
    override fun create(): RecipeListViewModel {
        return RecipeListViewModel(recipesRepository)
    }
}