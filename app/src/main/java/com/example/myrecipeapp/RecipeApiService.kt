package com.example.myrecipeapp

import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") categoryId: Int): List<Recipe>

    @GET("recipe/{id}")
    suspend fun getRecipeById(@Path("id") recipe: Int): Recipe

    @GET("recipes")
    suspend fun getRecipesByIds(@Query("ids") id: String): List<Recipe>
}