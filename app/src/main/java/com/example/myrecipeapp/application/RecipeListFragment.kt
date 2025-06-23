package com.example.myrecipeapp.application

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myrecipeapp.ARG_CATEGORY_ID
import com.example.myrecipeapp.ARG_CATEGORY_IMAGE_URL
import com.example.myrecipeapp.ARG_CATEGORY_NAME
import com.example.myrecipeapp.ARG_RECIPE
import com.example.myrecipeapp.R
import com.example.myrecipeapp.databinding.FragmentListRecipesBinding
import java.io.IOException

class RecipeListFragment : Fragment() {

    private var _binding: FragmentListRecipesBinding? = null
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null
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

        arguments?.let {
            categoryId = it.getInt(ARG_CATEGORY_ID)
            categoryName = it.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = it.getString(ARG_CATEGORY_IMAGE_URL)
        }

        binding.tvRecipes.text = categoryName

        try {
            categoryImageUrl?.let { fileName ->
                val inputStream = requireContext().assets.open(fileName)
                val bitMap = BitmapFactory.decodeStream(inputStream)
                binding.ivHeaderRecipe.setImageBitmap(bitMap)
                inputStream.close()
            }
        } catch (e: IOException) {
            Log.e("imageError", "Error image recipe")
        }

        initRecyclerRecipe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerRecipe() {

        categoryId?.let { id ->
            val recipeAdapter = RecipeListAdapter(STUB.getRecipesByCategoryId(id))
            binding.rvRecipeContainer.layoutManager = LinearLayoutManager(requireContext())
            binding.rvRecipeContainer.adapter = recipeAdapter

            recipeAdapter.setOnItemClickListenerRecipe(object :
                RecipeListAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {

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
}