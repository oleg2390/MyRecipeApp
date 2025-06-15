package com.example.myrecipeapp.application

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myrecipeapp.databinding.RecipesListFragmentBinding

class RecipesListFragment : Fragment() {

    private var _binding: RecipesListFragmentBinding? = null
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
        _binding = RecipesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            category_id = it.getInt(CategoriesListFragment.ARG_CATEGORY_ID)
            category_name = it.getString(CategoriesListFragment.ARG_CATEGORY_NAME)
            category_image_url = it.getString(CategoriesListFragment.ARG_CATEGORY_IMAGE_URL)
        }

        Log.d("data", "ID: $category_id, NAME: $category_name, IMAGE: $category_image_url")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}