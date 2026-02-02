package com.shuham.ganga.presentation.detail

import com.shuham.ganga.data.remote.model.ProductDto

data class ProductDetailState(
    val isLoading: Boolean = true,
    val product: ProductDto? = null,
    val selectedImageIndex: Int = 0,
    val errorMessage: String? = null,
    val addToCartMessage: String? = null,
    val navigateToCart: Boolean = false
)