package com.example.myrecipeapp.ui.recipes.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.ui.recipes.list_recipes.RecipeListAdapter
import com.example.myrecipeapp.databinding.FragmentFavoritesBinding

class FavoritesFragment() : Fragment() {

    private var recipeListAdapter = RecipeListAdapter()
    private val viewModel: FavoritesViewModel by viewModels()
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

        binding.rvFavoritesContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoritesContainer.adapter = recipeListAdapter

        recipeListAdapter.setOnItemClickListenerRecipe(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

        viewModel.toastMessage.observe(viewLifecycleOwner) { resId ->
            Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
        }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            initRecycle(state)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycle(state: FavoriteUiState) {

        state.recipes.let { recipes ->

            if (recipes.isEmpty()) {
                binding.rvFavoritesContainer.visibility = View.GONE
                binding.tvFavoritesEmpty.visibility = View.VISIBLE
            } else {
                binding.rvFavoritesContainer.visibility = View.VISIBLE
                binding.tvFavoritesEmpty.visibility = View.GONE
            }

            recipeListAdapter.updateAdapter(recipes)
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val navController = findNavController()
        val action = FavoritesFragmentDirections.actionFavoritesFragmentToRecipeFragment(recipeId)
        navController.navigate(action)
    }
}