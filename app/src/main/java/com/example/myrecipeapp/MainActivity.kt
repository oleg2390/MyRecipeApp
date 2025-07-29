package com.example.myrecipeapp

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.myrecipeapp.databinding.ActivityMainBinding
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.model.Recipe
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val currentNameMain = Thread.currentThread().name
        Log.i("!!!", "Метод onCreate() выполняется на потоке: $currentNameMain")

        val thread = Thread {
            try {
                val url = URL("https://recipes.androidsprint.ru/api/category")
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val currentName = Thread.currentThread().name

                Log.i("!!!", "Выполняю запрос на потоке: $currentName")

                val response = connection.inputStream.bufferedReader().use {
                    it.readText()
                }

                val category: List<Category> = Json.decodeFromString(response)
                Log.i("!!!", "Получено категорий: ${category.size}")

                category.forEach { category ->
                    threadPool.execute {
                        try {
                            val recipeUrl =
                                URL("https://recipes.androidsprint.ru/api/recipes?ids=${category.id}")
                            val connectionRecipeUrl =
                                recipeUrl.openConnection() as HttpURLConnection
                            connectionRecipeUrl.connect()

                            val responseRecipe = connectionRecipeUrl.inputStream.bufferedReader()
                                .use { it.readText() }
                            val recipe: List<Recipe> = Json.decodeFromString(responseRecipe)
                            Log.i("!!!", "Категория: ${category.title} - рецептов: ${recipe.size}")

                        } catch (e: Exception) {
                            Log.i(
                                "!!!",
                                "ошибка получения рецептов для категории ${category.title}: ${e.message}"
                            )
                        }
                    }
                }

            } catch (e: Exception) {
                Log.e("!!!", "Ошибка при запросе URL")
            }
        }

        thread.start()

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavourites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }
    }
}
