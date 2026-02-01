package com.shuham.ganga.presentation.dashboard.tabs.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ProductRepository
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
                // Debounce Search
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500) // Wait 500ms before API call
                    if (action.query.isNotEmpty()) {
                        performSearch(action.query)
                    }
                }
            }
            is SearchAction.OnSearchClick -> performSearch(action.query)
            SearchAction.OnBackClick -> { /* Handle in UI */ }
        }
    }

    private fun loadRecommended() {
        viewModelScope.launch {
            // Load initial "Recommended" feed (same as Home for now)
            val result = repository.getProducts(page = 1)
            if (result is NetworkResult.Success) {
                _state.update { it.copy(recommendedProducts = result.data ?: emptyList()) }
            }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Note: You need to implement search param in Repository later
            // For now, we reuse getProducts (backend handles filtering if supported or we mock it)
            val result = repository.getProducts(category = null) // Replace with search query later

            when (result) {
                is NetworkResult.Success -> {
                    // Client-side filter as temporary solution until backend search is perfect
                    val filtered = result.data?.filter {
                        it.title.contains(query, ignoreCase = true)
                    } ?: emptyList()

                    _state.update { it.copy(isLoading = false, searchResults = filtered) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                }
                else -> Unit
            }
        }
    }
}