package com.example.myrecipeapp.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.example.myrecipeapp.data.AppDatabase
import com.example.myrecipeapp.data.RecipeApiService
import com.example.myrecipeapp.data.RecipesRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class AppContainer(context: Context) {

    private val db: AppDatabase = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "database-categories"
    ).build()

    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    private val categoriesDao = db.categoriesDao()
    private val recipesDao = db.recipesDao()

    private val logging = HttpLoggingInterceptor { msg -> Log.d("!!!", msg) }
        .apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    private val contentType = "application/json".toMediaType()
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .client(client)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    private val recipeApiService: RecipeApiService = retrofit.create(RecipeApiService::class.java)
    private val repository = RecipesRepository(
        recipesDao = recipesDao,
        categoriesDao = categoriesDao,
        recipeApiService = recipeApiService,
        ioDispatcher = ioDispatcher,
    )

    val categoriesListViewModelFactory = CategoriesListViewModelFactory(repository)
    val recipeListViewModelFactory = RecipeListViewModelFactory(repository)
    val favoritesViewModelFactory = FavoritesViewModelFactory(repository)
    val recipeViewModelFactory = RecipeViewModelFactory(repository)
}