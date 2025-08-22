package com.example.myrecipeapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myrecipeapp.model.Recipe

@Dao
interface RecipesDao {

    @Query("SELECT * FROM recipes")
    suspend fun getRecipesDb(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipes: List<Recipe>)

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    suspend fun getFavoriteRecipe(): List<Recipe>

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavorite(recipeId: Int, isFavorite: Boolean): Int

    @Query("SELECT * FROM recipes WHERE id = :id LIMIT 1")
    suspend fun getRecipeById(id: Int): Recipe?
}