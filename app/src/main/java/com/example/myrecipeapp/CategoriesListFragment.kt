package com.example.myrecipeapp

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myrecipeapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("FragmentListCategoriesBinding must not be null")

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

//        val headerImage = binding.idHeader
//        val inputStreamImage = requireContext().assets.open ("bcg_categories.png")
//        val drawableImage = Drawable.createFromStream(inputStreamImage, null)
//        headerImage.setImageDrawable(drawableImage)
//
//        val radius =
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}