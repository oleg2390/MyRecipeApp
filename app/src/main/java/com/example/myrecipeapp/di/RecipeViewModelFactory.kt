package com.example.myrecipeapp.di

import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(recipesRepository)
    }
}