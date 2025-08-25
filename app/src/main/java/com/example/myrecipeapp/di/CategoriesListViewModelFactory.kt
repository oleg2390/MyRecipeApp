package com.example.myrecipeapp.di

import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.ui.categories.CategoriesListViewModel

class CategoriesListViewModelFactory(
    private val recipesRepository: RecipesRepository,
) : Factory<CategoriesListViewModel> {
    override fun create(): CategoriesListViewModel {
        return CategoriesListViewModel(recipesRepository)
    }
}