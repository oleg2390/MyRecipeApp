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
}