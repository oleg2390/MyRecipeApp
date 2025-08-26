package com.example.myrecipeapp.data

import android.util.Log
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RecipesRepository @Inject constructor(
    private val recipesDao: RecipesDao,
    private val categoriesDao: CategoriesDao,
    private val recipeApiService: RecipeApiService,
) {

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO

    private suspend fun getCategories(): List<Category>? = withContext(ioDispatcher) {
        try {
            val resultResponse = recipeApiService.getCategories()
            Log.d("!!!", "Получены категории: $resultResponse")
            resultResponse
        } catch (e: Exception) {
            Log.d("!!!", "getCategories exception", e)
            null
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> = withContext(ioDispatcher) {
        categoriesDao.getAllCategories()
    }

    suspend fun fetchAndSaveCategories(): Result<List<Category>> = runCatching {
        val categories = getCategories() ?: emptyList()

        if (categories.isNotEmpty()) {
            categoriesDao.insertCategories(categories)
        }

        categories
    }

    private suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(ioDispatcher) {
            try {
                val response = recipeApiService.getRecipesByCategoryId(categoryId)
                Log.e("!!!", "getRecipesByCategoryId HTTP $response")
                response
            } catch (e: Exception) {
                Log.d("!!!", "getRecipesByCategoryId exception $categoryId", e)
                null
            }
        }

    suspend fun getRecipeByCategoriesIdFromCash(): List<Recipe> =
        withContext(ioDispatcher) {
            recipesDao.getRecipesDb()
        }

    suspend fun fetchAndSaveRecipesByCategory(categoryId: Int): Result<List<Recipe>> = runCatching {
        val recipes = getRecipesByCategoryId(categoryId) ?: emptyList()
        if (recipes.isNotEmpty()) {
            recipesDao.insertRecipe(recipes)
        }
        recipes
    }

    suspend fun getFavoriteRecipe(): List<Recipe> =
        recipesDao.getFavoriteRecipe()

    suspend fun updateFavorite(recipeId: Int, isFavorite: Boolean) {
        recipesDao.updateFavorite(recipeId, isFavorite)
    }

    suspend fun getRecipeByIdFromDb(id: Int): Recipe? = withContext(ioDispatcher) {
        recipesDao.getRecipeById(id)
    }
}