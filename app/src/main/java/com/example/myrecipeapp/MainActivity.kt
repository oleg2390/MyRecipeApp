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
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {


    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
    private lateinit var binding: ActivityMainBinding
    private val json = Json { ignoreUnknownKeys = true }
    private val client: OkHttpClient by lazy {
        val loggin = HttpLoggingInterceptor { message ->
            Log.d("!!!", message)
        }.apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggin)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        val currentNameMain = Thread.currentThread().name
        Log.i("!!!", "Метод onCreate() выполняется на потоке: $currentNameMain")

        threadPool.execute {
            fetchCategoriesAndRecipe()
        }

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

    private fun fetchCategoriesAndRecipe() {

        try {
            val requestCategory: Request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .get()
                .build()

            client.newCall(requestCategory).execute().use { respose ->
                if (!respose.isSuccessful) {
                    Log.i("!!!", "Ошибка при получении категорий, responseCode: ${respose.code}")
                    return
                }

                val bodyString = respose.body?.string().orEmpty()
                val category: List<Category> = try {
                    json.decodeFromString(bodyString)
                } catch (e: Exception) {
                    Log.e("!!!", "Ошибка десериализации категорий: ${e.message}")
                    emptyList()
                }

                Log.i("!!!", "Получено категорий: ${category.size}")

                val futures = category.map { category ->
                    threadPool.submit {
                        try {
                            val recipeUrl =
                                URL("https://recipes.androidsprint.ru/api/recipes?ids=${category.id}")
                            val requestRecipe: Request = Request.Builder()
                                .url(recipeUrl)
                                .get()
                                .build()

                            client.newCall(requestRecipe).execute().use { respose ->
                                if (!respose.isSuccessful) {
                                    Log.e(
                                        "!!!",
                                        "Категория '${category.title}': ошибка HTTP ${respose.code}"
                                    )
                                    return@submit
                                }

                                val recipeBody = respose.body?.string().orEmpty()
                                val recipes: List<Recipe> = try {
                                    json.decodeFromString(recipeBody)
                                } catch (e: Exception) {
                                    Log.e(
                                        "!!!",
                                        "Категория '${category.title}': ошибка парсинга рецептов: ${e.message}"
                                    )
                                    emptyList()
                                }
                                Log.i(
                                    "!!!",
                                    "Категория: ${category.title}, рецептов: ${recipes.size}"
                                )
                            }
                        } catch (e: Exception) {
                            Log.e(
                                "!!!",
                                "Ошибка загрузки рецептов для категории '${category.title}': ${e.message}"
                            )
                        }
                    }
                }
                futures.forEach { future ->
                    try {
                        future.get()
                    } catch (e: Exception) {
                        Log.e("!!!", "Ошибка рецептов: ${e.message}")
                    }
                }
                Log.i("!!!", "Все загрузки рецептов завершены")
            }
        } catch (e: Exception) {
            Log.e("!!!", "Ошибка при запросе категорий: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}
