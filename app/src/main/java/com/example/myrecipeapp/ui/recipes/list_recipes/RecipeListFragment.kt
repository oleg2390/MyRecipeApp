package com.example.myrecipeapp.ui.recipes.list_recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.databinding.FragmentListRecipesBinding

class RecipeListFragment : Fragment() {

    private var recipeListAdapter = RecipeListAdapter()
    private val viewModel: RecipeListViewModel by viewModels()
    private var _binding: FragmentListRecipesBinding? = null

    private val binding
        get() = _binding
            ?: throw IllegalStateException("RecipesListFragmentBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvRecipeContainer.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRecipeContainer.adapter = recipeListAdapter
        recipeListAdapter.setOnItemClickListenerRecipe(object :
            RecipeListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.tvRecipes.text = state.categoryName
            binding.ivHeaderRecipe.setImageDrawable(state.categoryImage)
            recipeListAdapter.updateAdapter(state.recipes)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun openRecipeByRecipeId(recipeId: Int) {

        val action = RecipeListFragmentDirections.actionRecipeListFragmentToRecipeFragment(recipeId)
        findNavController().navigate(action)
    }
}