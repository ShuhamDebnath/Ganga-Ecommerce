package com.shuham.ganga.presentation.dashboard.tabs.search

import com.shuham.ganga.data.remote.model.ProductDto

data class SearchState(
    val query: String = "",
    val searchResults: List<ProductDto> = emptyList(),
    val popularSearches: List<String> = listOf("Fossil Watch", "iPhone 14 Pro", "Gaming Chair", "New Balance"),
    val recommendedProducts: List<ProductDto> = emptyList(), // Fallback/Initial content
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)