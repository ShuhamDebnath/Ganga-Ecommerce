package com.shuham.ganga.presentation.dashboard.tabs.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.domain.usecase.GetProductsUseCase
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val getProductsUseCase: GetProductsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadRecommended()
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnQueryChange -> {
                _state.update { it.copy(query = action.query) }
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500)
                    if (action.query.isNotEmpty()) {
                        performSearch(action.query)
                    }
                }
            }
            is SearchAction.OnSearchClick -> performSearch(action.query)
            SearchAction.OnBackClick -> { }
        }
    }

    private fun loadRecommended() {
        // UseCase invocation
        getProductsUseCase(page = 1).onEach { result ->
            if (result is NetworkResult.Success<List<Product>>) {
                _state.update { it.copy(recommendedProducts = result.data ?: emptyList()) }
            }
        }.launchIn(viewModelScope)
    }

    private fun performSearch(query: String) {
        // UseCase invocation
        getProductsUseCase(query = query).onEach { result ->
            when (result) {
                is NetworkResult.Loading<*> -> {
                    _state.update { it.copy(isLoading = true) }
                }
                is NetworkResult.Success<List<Product>> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            searchResults = result.data ?: emptyList()
                        )
                    }
                }
                is NetworkResult.Error<List<Product>> -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }
}