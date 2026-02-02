package com.shuham.ganga.presentation.dashboard.tabs.search

import com.shuham.ganga.data.remote.model.ProductDto
import com.shuham.ganga.domain.model.Product

data class SearchState(
    val query: String = "",
    val searchResults: List<Product> = emptyList(),
    val popularSearches: List<String> = listOf("Fossil Watch", "iPhone 14 Pro", "Gaming Chair", "New Balance"),
    val recommendedProducts: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)