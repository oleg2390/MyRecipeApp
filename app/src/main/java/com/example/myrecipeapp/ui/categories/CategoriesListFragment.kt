package com.example.myrecipeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myrecipeapp.ARG_CATEGORY_ID
import com.example.myrecipeapp.ARG_CATEGORY_IMAGE_URL
import com.example.myrecipeapp.ARG_CATEGORY_NAME
import com.example.myrecipeapp.R
import com.example.myrecipeapp.databinding.FragmentListCategoriesBinding
import com.example.myrecipeapp.model.Category

class CategoriesListFragment : Fragment() {

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

        val adapter = CategoriesListAdapter { category ->
            openRecipesByCategoryId(category)
        }
        binding.rvCategories.adapter = adapter
        viewModel.state.observe(viewLifecycleOwner) {state ->
            adapter.updateAdapter(state.categories)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipesByCategoryId(category: Category) {

        val bundle = Bundle().apply {
            putInt(ARG_CATEGORY_ID, category.id)
            putString(ARG_CATEGORY_NAME, category.title)
            putString(ARG_CATEGORY_IMAGE_URL, category.imageUrl)
        }

        findNavController().navigate(R.id.action_categoriesListFragment_to_recipeListFragment, bundle)
    }
}