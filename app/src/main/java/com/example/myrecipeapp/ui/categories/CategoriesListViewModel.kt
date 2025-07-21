package com.example.myrecipeapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Category

data class CategoriesUiState(
    val categories: List<Category> = emptyList()
)

class CategoriesListViewModel : ViewModel() {
    private val _state = MutableLiveData<CategoriesUiState>()
    val state: LiveData<CategoriesUiState> = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {
        val data = STUB.getCategory()
        val currentCategories = state.value ?: CategoriesUiState()
        val newStateCategories = currentCategories.copy(
            categories = data
        )
        _state.value = newStateCategories
    }
}