package com.shuham.ganga.presentation.dashboard.tabs.home

import com.shuham.ganga.domain.model.Product

data class HomeState(
    val isLoading: Boolean = false,
    val products: List<Product> = emptyList(),
    val errorMessage: String? = null
)