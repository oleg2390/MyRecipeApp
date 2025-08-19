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

            try {
                val cachedCategory = repository.getCategoriesFromCache()
                _state.postValue(CategoriesUiState(cachedCategory))

                val result = repository.fetchAndSaveCategories()

                result.fold(
                    onSuccess = { networkCategories ->
                        Log.i("!!!", "Категории загружены: - $networkCategories")
                        _state.postValue(CategoriesUiState(networkCategories))
                    },
                    onFailure = { error ->
                        Log.e("!!!", "Ошибка загрузки категорий: ${error.message}")
                        toastMessage.postValue(R.string.errorToast)
                    }
                )
            } catch (e: Exception) {
                Log.e("!!!", "Ошибка при загрузке категорий: ${e.message}")
                toastMessage.postValue(R.string.errorToast)
            }
        }
    }
}