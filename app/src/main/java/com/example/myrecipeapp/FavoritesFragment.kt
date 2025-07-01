package com.example.myrecipeapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.application.RecipeFragment
import com.example.myrecipeapp.application.RecipeListAdapter
import com.example.myrecipeapp.application.STUB
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

        val favoritesId: Set<Int> = getFavorites()
        val favoriteRecipes = STUB.getRecipesByIds(favoritesId)

        if (favoriteRecipes.isEmpty()) {
            binding.rvFavoritesContainer.visibility = View.GONE
            binding.tvFavoritesEmpty.visibility = View.VISIBLE
        }else {
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

        val recipe = STUB.getRecipeById(recipeId)
        val bundle = Bundle().apply {
            putParcelable(ARG_RECIPE, recipe)
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }

    private fun getFavorites(): Set<Int> {
        val sharedPrefs = requireContext().getSharedPreferences(
            getString(R.string.favorite),
            Context.MODE_PRIVATE
        )
        val storedSet = sharedPrefs.getStringSet(getString(R.string.favorite_recipe), emptySet())
        return storedSet
            ?.mapNotNull { it.toIntOrNull() }
            ?.toSet()
            ?: emptySet()
    }
}