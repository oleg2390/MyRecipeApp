package com.example.myrecipeapp.data

import android.util.Log
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RecipesRepository {

    private val api: RecipeApiService
    private val databaseCategories = MyApp.database.categoriesDao()
    private val databaseRecipe = MyApp.database.recipesDao()

    init {

        val logging = HttpLoggingInterceptor { msg -> Log.d("!!!", msg) }
        logging.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        val json = Json { ignoreUnknownKeys = true }

        val contentType = "application/json".toMediaType()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://recipes.androidsprint.ru/api/")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()

        api = retrofit.create(RecipeApiService::class.java)
    }

    private suspend fun getCategories(): List<Category>? = withContext(Dispatchers.IO) {
        try {
            val resultResponse = api.getCategories()
            Log.d("!!!", "Получены категории: $resultResponse")
            resultResponse
        } catch (e: Exception) {
            Log.d("!!!", "getCategories exception", e)
            null
        }
    }

    suspend fun getCategoriesFromCache(): List<Category> = withContext(Dispatchers.IO) {
        databaseCategories.getAllCategories()
    }

    suspend fun fetchAndSaveCategories(): Result<List<Category>> = runCatching {
        val categories = getCategories() ?: emptyList()

        if (categories.isNotEmpty()) {
            databaseCategories.insertCategories(categories)
        }

        categories
    }

    private suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? =
        withContext(Dispatchers.IO) {
            try {
                val response = api.getRecipesByCategoryId(categoryId)
                Log.e("!!!", "getRecipesByCategoryId HTTP $response")
                response
            } catch (e: Exception) {
                Log.d("!!!", "getRecipesByCategoryId exception $categoryId", e)
                null
            }
        }

    suspend fun getRecipeByCategoriesIdFromCash(): List<Recipe> =
        withContext(Dispatchers.IO) {
            databaseRecipe.getRecipesDb()
        }

    suspend fun fetchAndSaveRecipesByCategory(categoryId: Int): Result<List<Recipe>> = runCatching {
        val recipes = getRecipesByCategoryId(categoryId) ?: emptyList()
        if (recipes.isNotEmpty()) {
            databaseRecipe.insertRecipe(recipes)
        }
        recipes
    }

    suspend fun getFavoriteRecipe(): List<Recipe> =
        databaseRecipe.getFavoriteRecipe()

    suspend fun updateFavorite(recipeId: Int, isFavorite: Boolean) {
        databaseRecipe.updateFavorite(recipeId, isFavorite)
    }

    suspend fun getRecipeByIdFromDb(id: Int): Recipe? = withContext(Dispatchers.IO) {
        databaseRecipe.getRecipeById(id)
    }
}