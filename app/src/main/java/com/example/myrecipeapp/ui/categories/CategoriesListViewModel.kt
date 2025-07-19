package com.example.myrecipeapp.ui.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipeapp.data.STUB
import com.example.myrecipeapp.model.Category

class CategoriesListViewModel: ViewModel() {
    private val _state = MutableLiveData<List<Category>>()
    val state : LiveData<List<Category>> = _state

    init {
        loadCategories()
    }

    private fun loadCategories() {
        val data = STUB.getCategory()
        _state.value = data
    }
}