package com.example.myrecipeapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myrecipeapp.R
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.model.Category
import kotlinx.coroutines.launch

data class CategoriesUiState(
    val categories: List<Category> = emptyList()
)

class CategoriesListViewModel : ViewModel() {
    private val _state = MutableLiveData<CategoriesUiState>()
    val state: LiveData<CategoriesUiState> = _state
    private val repository = RecipesRepository()
    var toastMessage = SingleLiveEvent<Int>()

    init {
        loadCategories()
    }

    private fun loadCategories() {

        viewModelScope.launch {
            val result = repository.getCategories()
            if (result == null) {
                toastMessage.postValue(R.string.errorToast)
            } else {
                Log.i("!!!", "Категории загружены: - $result")
                _state.postValue(CategoriesUiState(result))
            }
        }
    }
}