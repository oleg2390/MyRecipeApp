package com.example.myrecipeapp.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myrecipeapp.RecipeApplication
import com.example.myrecipeapp.databinding.FragmentListCategoriesBinding
import com.example.myrecipeapp.model.Category

class CategoriesListFragment : Fragment() {

    private lateinit var categoriesListViewModel: CategoriesListViewModel
    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("FragmentListCategoriesBinding must not be null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireActivity().application as RecipeApplication).appContainer
        categoriesListViewModel = appContainer.categoriesListViewModelFactory.create()
    }

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

        categoriesListViewModel.toastMessage.observe(viewLifecycleOwner) { resId ->
            Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
        }

        categoriesListViewModel.state.observe(viewLifecycleOwner) { state ->
            adapter.updateAdapter(state.categories)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openRecipesByCategoryId(category: Category) {

        val foundCategory =
            categoriesListViewModel.state.value?.categories?.find { it.id == category.id }
                ?: throw IllegalArgumentException("Category id = ${category.id} not found")

        val action =
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipeListFragment(
                foundCategory
            )

        findNavController().navigate(action)
    }
}