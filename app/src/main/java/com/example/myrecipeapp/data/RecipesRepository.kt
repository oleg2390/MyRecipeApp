package com.example.myrecipeapp.data

import android.util.Log
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RecipesRepository {

    private val api: RecipeApiService

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

    suspend fun getCategories(): List<Category>? {

        return try {
            val result = api.getCategories()
            Log.d("!!!", "Получены категории: $result")
            result
        } catch (e: Exception) {
            Log.d("!!!", "getCategories exception", e)
            null
        }
    }

    suspend fun getRecipesByCategoryId(categoryId: Int): List<Recipe>? {
        return try {
            val response = api.getRecipesByCategoryId(categoryId)
            Log.d("!!!", "getRecipesByCategoryId ($categoryId) failed: HTTP $response ")
            response
        } catch (e: Exception) {
            Log.d("!!!", "getRecipesByCategoryId exception $categoryId", e)
            null
        }
    }

    suspend fun getRecipeById(recipeId: Int): Recipe? {
        return try {
            val response = api.getRecipeById(recipeId)
            Log.d("!!!", "getRecipeById($recipeId) failed: HTTP ${response}")
            response
        } catch (e: Exception) {
            Log.d("!!!", "getRecipeById exception for $recipeId")
            null
        }
    }

    suspend fun getRecipesByIds(ids: Set<Int>): List<Recipe>? {
        return try {
            val idsParam = ids.joinToString(",")
            val response = api.getRecipesByIds(idsParam)
            Log.d("!!!", "getRecipesByIds($ids) failed: HTTP ${response}")
            response
        } catch (e: Exception) {
            Log.d("!!!", "getRecipesByIds exception for $ids")
            null
        }
    }
}