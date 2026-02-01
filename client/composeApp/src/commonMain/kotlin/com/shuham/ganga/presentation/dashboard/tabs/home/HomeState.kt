package com.shuham.ganga.presentation.dashboard.tabs.home

import com.shuham.ganga.data.remote.model.ProductDto

data class HomeState(
    val isLoading: Boolean = false,
    val products: List<ProductDto> = emptyList(),
    val errorMessage: String? = null
)