package com.example.myrecipeapp.ui.categories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myrecipeapp.R
import com.example.myrecipeapp.SingleLiveEvent
import com.example.myrecipeapp.data.RecipesRepository
import com.example.myrecipeapp.model.Category

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

        repository.getCategories { result ->
            if (result == null) {
                toastMessage.postValue(R.string.errorToast)
            } else {
                Log.i("!!!", "Категории загружены: - $result")
                _state.postValue(CategoriesUiState(result))
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.shutdown()
    }
}