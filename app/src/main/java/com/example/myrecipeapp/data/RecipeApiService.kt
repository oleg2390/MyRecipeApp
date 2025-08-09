package com.example.myrecipeapp.data

import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {

    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") categoryId: Int): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") recipe: Int): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") id: String): Call<List<Recipe>>
}