package com.example.myrecipeapp.ui.recipes.recipe

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.R
import com.example.myrecipeapp.data.ImageLoader
import com.example.myrecipeapp.databinding.FragmentRecipesBinding
import com.google.android.material.divider.MaterialDividerItemDecoration

class RecipeFragment : Fragment() {

    private var ingredientsAdapter = IngredientsAdapter()
    private var methodAdapter = MethodAdapter()
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

        val args: RecipeFragmentArgs by navArgs()
        val categoryId = args.recipeId

        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.layoutManager = LinearLayoutManager(requireContext())
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        binding.rvIngredients.addItemDecoration(getDivider())
        binding.rvMethod.addItemDecoration(getDivider())

        viewModel.loadRecipe(categoryId)

        viewModel.toastMessage.observe(viewLifecycleOwner) { resId ->
            Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
        }

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
            binding.tvRecipeFragmentPortionsNumber.text = state.portions.toString()

            ingredientsAdapter.ingredients = recipe.ingredients
            ingredientsAdapter.updateIngredients(state.portions)

            methodAdapter.method = recipe.method

            state.recipeImage?.let { imageName ->
                ImageLoader.loadImage(
                    requireContext(),
                    imageName,
                    binding.ivHeaderRecipeFragment
                )
            }

            updateFavoriteIcon(state.isFavorites)

            binding.sbRecipeFragment.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
                viewModel.onPortionsChanged(progress)
            })

            binding.ibRecipeFragmentFavoriteButton.setOnClickListener {
                viewModel.onFavoritesClicked()
            }
        }
    }

    private fun updateFavoriteIcon(favorite: Boolean) {
        val iconRes = if (favorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        binding.ibRecipeFragmentFavoriteButton.setImageResource(iconRes)
    }

    private fun getDivider(): MaterialDividerItemDecoration {
        return MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        ).apply {
            isLastItemDecorated = false
            dividerColor = ContextCompat.getColor(requireContext(), R.color.line_item_color)
            dividerThickness = resources.getDimensionPixelSize(R.dimen.dimens_1dp)
            dividerInsetStart = resources.getDimensionPixelSize(R.dimen.dimens_12dp)
            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen.dimens_12dp)
        }
    }
}

class PortionSeekBarListener(
    val onChangeIngredients: (Int) -> Unit
) : OnSeekBarChangeListener {
    override fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    ) {

        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}
    override fun onStopTrackingTouch(p0: SeekBar?) {}
}