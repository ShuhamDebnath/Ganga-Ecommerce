package com.shuham.ganga.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: ProductDetailAction) {
        when (action) {
            is ProductDetailAction.LoadProduct -> loadProduct(action.id)
            is ProductDetailAction.OnImageChange -> {
                _state.update { it.copy(selectedImageIndex = action.index) }
            }
            ProductDetailAction.OnAddToCart -> {
                // TODO: Implement Cart Add Logic
            }
            else -> Unit
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getProductById(id)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, product = result.data) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}