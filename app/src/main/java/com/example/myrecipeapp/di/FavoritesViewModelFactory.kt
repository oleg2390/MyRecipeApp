package com.example.myrecipeapp.di

import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.ui.recipes.favorite.FavoritesViewModel

class FavoritesViewModelFactory(
    private val recipesRepository: RecipesRepository
) : Factory<FavoritesViewModel> {
    override fun create(): FavoritesViewModel {
        return FavoritesViewModel(recipesRepository)
    }
}