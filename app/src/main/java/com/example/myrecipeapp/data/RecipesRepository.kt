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
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class RecipesRepository {

    private val api: RecipeApiService
    private val threadPoll: ExecutorService = Executors.newFixedThreadPool(10)

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

    fun getCategories(callback: (List<Category>?) -> Unit) {
        threadPoll.execute {
            val result = try {
                val resultResponse = api.getCategories().execute().body()
                Log.d("!!!", "Получены категории: $resultResponse")
                resultResponse
            } catch (e: Exception) {
                Log.d("!!!", "getCategories exception", e)
                null
            }
            callback(result)
        }
    }

    fun getRecipesByCategoryId(categoryId: Int, callback: (List<Recipe>?) -> Unit) {
        threadPoll.execute {
            val result = try {
                val call = api.getRecipesByCategoryId(categoryId)
                val response = call.execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("!!!", "getRecipesByCategoryId HTTP ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.d("!!!", "getRecipesByCategoryId exception $categoryId", e)
                null
            }
            callback(result)
        }
    }

    fun getRecipeById(recipeId: Int, callback: (Recipe?) -> Unit) {
        threadPoll.execute {
            val response = try {
                val call = api.getRecipeById(recipeId)
                val response = call.execute()
                if (response.isSuccessful) {
                    Log.d("!!!", "getRecipeById($recipeId) failed: HTTP ${response.body()}")
                    response.body()
                } else {
                    Log.e("!!!", "getRecipeById HTTP ${response.code()}")
                    null
                }
            } catch (e: Exception) {
                Log.d("!!!", "getRecipeById exception for $recipeId")
                null
            }
            callback(response)
        }
    }

    fun getRecipesByIds(ids: Set<Int>, callback: (List<Recipe>?) -> Unit) {

        threadPoll.execute {
            val response = try {
                val idsParam = ids.joinToString(",")
                val call = api.getRecipesByIds(idsParam)
                val response = call.execute()
                Log.d("!!!", "getRecipesByIds($ids) failed: HTTP ${response.code()}")
                response.body()
            } catch (e: Exception) {
                Log.d("!!!", "getRecipesByIds exception for $ids")
                null
            }
            callback(response)
        }
    }
}