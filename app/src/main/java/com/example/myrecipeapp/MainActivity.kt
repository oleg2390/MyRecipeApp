package com.example.myrecipeapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myrecipeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, CategoriesListFragment())
                .commit()
        }

        binding.btnCategory.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, CategoriesListFragment())
                .commit()
        }

        binding.btnFavourites.setOnClickListener {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.mainContainer, FavoritesFragment())
                .commit()
        }
    }
}
