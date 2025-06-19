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
    private var category_id: Int? = null
    private var category_name: String? = null
    private var category_image_url: String? = null
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
            category_id = it.getInt(ARG_CATEGORY_ID)
            category_name = it.getString(ARG_CATEGORY_NAME)
            category_image_url = it.getString(ARG_CATEGORY_IMAGE_URL)
        }

        binding.tvRecipes.text = category_name

        try {
            category_image_url?.let { fileName ->
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

        category_id?.let { id ->
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