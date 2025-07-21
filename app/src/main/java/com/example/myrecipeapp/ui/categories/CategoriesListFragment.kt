package com.example.myrecipeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.example.myrecipeapp.ARG_CATEGORY_ID
import com.example.myrecipeapp.ARG_CATEGORY_IMAGE_URL
import com.example.myrecipeapp.ARG_CATEGORY_NAME
import com.example.myrecipeapp.R
import com.example.myrecipeapp.databinding.FragmentListCategoriesBinding
import com.example.myrecipeapp.model.Category
import com.example.myrecipeapp.ui.recipes.list_recipes.RecipeListFragment

class CategoriesListFragment : Fragment() {

    private lateinit var categoriesListAdapter: CategoriesListAdapter
    private val viewModel: CategoriesListViewModel by viewModels()
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
        viewModel.state.observe(viewLifecycleOwner) { state ->
            categoriesListAdapter.updateAdapter(state.categories)

            categoriesListAdapter.setOnItemClickListener(object :
                CategoriesListAdapter.OnItemClickListener {
                override fun onItemClick(categoryId: Int) {
                    val category = state.categories.find { it.id == categoryId }
                    openRecipesByCategoryId(category)
                }
            })
        }

        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipesByCategoryId(category: Category?) {

        if (category == null) return

        val bundle = Bundle().apply {
            putInt(ARG_CATEGORY_ID, category.id)
            putString(ARG_CATEGORY_NAME, category.title)
            putString(ARG_CATEGORY_IMAGE_URL, category.imageUrl)
        }

        parentFragmentManager.commit {
            setReorderingAllowed(true)
            replace<RecipeListFragment>(R.id.mainContainer, args = bundle)
            addToBackStack(null)
        }
    }

    private fun initRecyclerView() {
        categoriesListAdapter = CategoriesListAdapter()
        binding.rvCategories.adapter = categoriesListAdapter

    }
}