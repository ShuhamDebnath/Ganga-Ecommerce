package com.shuham.ganga.presentation.dashboard.tabs.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.domain.usecase.GetProductsUseCase
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class HomeViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        loadProducts()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.OnRefresh -> loadProducts()
            else -> Unit
        }
    }

    private fun loadProducts() {
        getProductsUseCase().onEach { result ->
            when (result) {
                is NetworkResult.Loading<*> -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Success<List<Product>> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            products = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error<List<Product>> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message,
                            products = result.data ?: emptyList()
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}