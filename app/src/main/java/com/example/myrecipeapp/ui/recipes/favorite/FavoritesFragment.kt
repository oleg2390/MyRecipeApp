package com.example.myrecipeapp.ui.recipes.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.ARG_RECIPE
import com.example.myrecipeapp.R
import com.example.myrecipeapp.data.AppPreferences
import com.example.myrecipeapp.ui.recipes.recipe.RecipeFragment
import com.example.myrecipeapp.ui.recipes.list_recipes.RecipeListAdapter
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.databinding.FragmentFavoritesBinding

class FavoritesFragment() : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentFavoritesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle() {

        val appPreferences = AppPreferences(requireContext())
        val favoritesId: Set<Int> = appPreferences
            .getFavorites()
            .mapNotNull { it.toIntOrNull() }
            .toSet()
        val favoriteRecipes = STUB.getRecipesByIds(favoritesId)

        if (favoriteRecipes.isEmpty()) {
            binding.rvFavoritesContainer.visibility = View.GONE
            binding.tvFavoritesEmpty.visibility = View.VISIBLE
        } else {
            binding.rvFavoritesContainer.visibility = View.VISIBLE
            binding.tvFavoritesEmpty.visibility = View.GONE
        }

        val adapter = RecipeListAdapter(favoriteRecipes)
        binding.rvFavoritesContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoritesContainer.adapter = adapter

        adapter.setOnItemClickListenerRecipe(object : RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val bundle = Bundle().apply {
            putInt(ARG_RECIPE, recipeId)
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}