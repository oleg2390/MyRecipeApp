package com.example.myrecipeapp.application

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.ARG_RECIPE
import com.example.myrecipeapp.R
import com.example.myrecipeapp.databinding.FragmentRecipesBinding
import com.example.myrecipeapp.models.Recipe
import java.io.IOException

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("RecipeFragmentBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATED")
            arguments?.getParcelable<Recipe>(ARG_RECIPE)
        }

        if (recipe != null) {
            binding.tvRecipeFragmentTittle.text = recipe.title

            try {
                val assetManager = binding.root.context.assets

                assetManager.open(recipe.imageUrl).use { inputStream ->
                    val drawable = Drawable.createFromStream(inputStream, null)
                    binding.ivHeaderRecipeFragment.setImageDrawable(drawable)
                }

            } catch (e: IOException) {
                Log.e("image", "image upload error from assets", e)
                binding.ivHeaderRecipeFragment.setImageResource(R.drawable.burger)
            }

        } else {
            Toast.makeText(requireContext(), "Рецепт не найден", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }

        initRecyclerRecipe()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initRecyclerRecipe() {

        val recipe = arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        val ingredientAdapter = recipe?.let { IngredientsAdapter(it.ingredients) }
        binding.rvIngredients.adapter = ingredientAdapter
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initUI() {
        val method = arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        val methodAdapter = method?.let { MethodAdapter(it.method) }
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.layoutManager = LinearLayoutManager(requireContext())
    }
}