package com.example.myrecipeapp.data

import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApiService {

    @GET("category")
    suspend fun getCategories(): List<Category>

    @GET("category/{id}/recipes")
    suspend fun getRecipesByCategoryId(@Path("id") categoryId: Int): List<Recipe>
}