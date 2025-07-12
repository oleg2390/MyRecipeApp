package com.example.myrecipeapp.ui.recipes.recipe

import android.os.Build
import android.os.Bundle
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
import com.example.myrecipeapp.databinding.FragmentRecipesBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

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

        val recipeId = arguments?.getInt(ARG_RECIPE)
        if (recipeId == null) {
            Toast.makeText(requireContext(), R.string.text_recipe_error, Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
            return
        }

        viewModel.loadRecipe(recipeId)
        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            initUI(state)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun initUI(state: RecipeUiState) {

        state.recipe?.let { recipe ->
            binding.tvRecipeFragmentTittle.text = recipe.title

            val stateImage = state.recipeImage
            if (stateImage != null) {
                binding.ivHeaderRecipeFragment.setImageDrawable(stateImage)
            } else {
                binding.ivHeaderRecipeFragment.setImageResource(R.drawable.burger)
            }

            updateFavoriteIcon(state.isFavorites)

            binding.tvRecipeFragmentPortionsNumber.text = state.portions.toString()

            val ingredientsAdapter = IngredientsAdapter(recipe.ingredients).apply {
                updateIngredients(state.portions)
            }

            val methodAdapter = MethodAdapter(recipe.method)
            binding.rvMethod.adapter = methodAdapter
            binding.rvMethod.layoutManager = LinearLayoutManager(requireContext())
            binding.rvIngredients.adapter = ingredientsAdapter
            binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())

            val divider =
                MaterialDividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                ).apply {
                    isLastItemDecorated = false
                    dividerColor = ContextCompat.getColor(requireContext(), R.color.line_item_color)
                    dividerThickness = resources.getDimensionPixelSize(R.dimen.dimens_1dp)
                    dividerInsetStart = resources.getDimensionPixelSize(R.dimen.dimens_12dp)
                    dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.dimens_12dp)
                }

            binding.rvIngredients.addItemDecoration(divider)
            binding.rvMethod.addItemDecoration(divider)

            binding.sbRecipeFragment.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    binding.tvRecipeFragmentPortionsNumber.text = progress.toString()
                    ingredientsAdapter.updateIngredients(progress)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {}
                override fun onStopTrackingTouch(p0: SeekBar?) {}
            })

            binding.ibRecipeFragmentFavoriteButton.setOnClickListener {
                viewModel.onFavoritesClicked()
                val messageToast =
                    getString(if (!state.isFavorites) R.string.add_favorite else R.string.remove_favorite)

                Toast.makeText(
                    requireContext(),
                    messageToast,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateFavoriteIcon(favorite: Boolean) {
        val iconRes = if (favorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        binding.ibRecipeFragmentFavoriteButton.setImageResource(iconRes)
    }
}