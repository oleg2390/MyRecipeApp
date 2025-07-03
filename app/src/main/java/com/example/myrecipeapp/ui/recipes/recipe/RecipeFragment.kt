package com.example.myrecipeapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.ARG_RECIPE
import com.example.myrecipeapp.R
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.databinding.FragmentRecipesBinding
import com.example.myrecipeapp.model.Recipe
import com.google.android.material.divider.MaterialDividerItemDecoration
import java.io.IOException

class RecipeFragment : Fragment() {

    private val viewModel: RecipeViewModel by viewModels()
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

        viewModel.uiState.observe(viewLifecycleOwner) {
            Log.i("!!!", "isFavorite: ${it.isFavorites}")
        }

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATED")
            arguments?.getParcelable(ARG_RECIPE)
        }

        initRecycler()
        initUI(recipe)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initRecycler() {

        val recipe = arguments?.getParcelable(ARG_RECIPE, Recipe::class.java)
        val ingredientAdapter = recipe?.let { IngredientsAdapter(it.ingredients) }
        val methodAdapter = recipe?.let { MethodAdapter(it.method) }
        val recyclerViewIngredient = binding.rvIngredients
        val recyclerViewMethod = binding.rvMethod
        val divider =
            MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL).apply {
                isLastItemDecorated = false
                dividerColor = ContextCompat.getColor(requireContext(), R.color.line_item_color)
                dividerThickness = resources.getDimensionPixelSize(R.dimen.dimens_1dp)
                dividerInsetStart = resources.getDimensionPixelSize(R.dimen.dimens_12dp)
                dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.dimens_12dp)
            }

        recyclerViewIngredient.adapter = ingredientAdapter
        recyclerViewIngredient.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewMethod.adapter = methodAdapter
        recyclerViewMethod.layoutManager = LinearLayoutManager(requireContext())

        recyclerViewIngredient.addItemDecoration(divider)
        recyclerViewMethod.addItemDecoration(divider)

        binding.sbRecipeFragment.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvRecipeFragmentPortionsNumber.text = progress.toString()
                ingredientAdapter?.updateIngredients(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initUI(recipe: Recipe?) {

        if (recipe == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.text_recipe_error), Toast.LENGTH_SHORT
            ).show()
            parentFragmentManager.popBackStack()
            return
        }

        binding.tvRecipeFragmentTittle.text = recipe.title

        try {
            val assetManager = binding.root.context.assets
            assetManager.open(recipe.imageUrl).use { inputStream ->
                val drawable = Drawable.createFromStream(inputStream, null)
                binding.ivHeaderRecipeFragment.setImageDrawable(drawable)
            }
        } catch (e: IOException) {
            Log.e("image", getString(R.string.image_upload_error_from_assets), e)
            binding.ivHeaderRecipeFragment.setImageResource(R.drawable.burger)
        }

        val appPreferences = AppPreferences(requireContext())
        val recipeId = recipe.id.toString()
        var isCurrentlyFavorite = recipeId in appPreferences.getFavorites()
        updateFavoriteIcon(isCurrentlyFavorite)

        binding.ibRecipeFragmentFavoriteButton.setOnClickListener {

            val favorites = appPreferences.getFavorites().toMutableSet()
            if (favorites.contains(recipeId)) {
                favorites.remove(recipeId)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.remove_favorite),
                    Toast.LENGTH_SHORT
                ).show()
                isCurrentlyFavorite = false
            } else {
                favorites.add(recipeId)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.add_favorite),
                    Toast.LENGTH_SHORT
                ).show()
                isCurrentlyFavorite = true
            }

            appPreferences.saveFavorites(favorites)
            updateFavoriteIcon(isCurrentlyFavorite)
        }
    }

    private fun updateFavoriteIcon(favorite: Boolean) {
        val iconRes = if (favorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        binding.ibRecipeFragmentFavoriteButton.setImageResource(iconRes)
    }
}