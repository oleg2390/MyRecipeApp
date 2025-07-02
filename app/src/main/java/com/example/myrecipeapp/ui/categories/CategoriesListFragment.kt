package com.example.myrecipeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.myrecipeapp.ARG_CATEGORY_ID
import com.example.myrecipeapp.ARG_CATEGORY_IMAGE_URL
import com.example.myrecipeapp.ARG_CATEGORY_NAME
import com.example.myrecipeapp.R
import com.example.myrecipeapp.databinding.FragmentListCategoriesBinding
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.ui.recipes.list_recipes.RecipeListFragment

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("FragmentListCategoriesBinding must not be null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {

        val categoriesAdapter = CategoriesListAdapter(STUB.getCategory())
        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {

        val category = STUB.getCategory().find { it.id == categoryId }
        val categoryName = category?.title ?: return
        val categoryImageUrl = category.imageUrl

        val bundle = Bundle().apply {
            putInt(ARG_CATEGORY_ID, categoryId)
            putString(ARG_CATEGORY_NAME, categoryName)
            putString(ARG_CATEGORY_IMAGE_URL, categoryImageUrl)
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeListFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }
}