package com.shuham.ganga.presentation.detail

import com.shuham.ganga.domain.model.Product

data class ProductDetailState(
    val isLoading: Boolean = true,
    val product: Product? = null,
    val isWishlisted: Boolean = false,
    val selectedImageIndex: Int = 0,
    val errorMessage: String? = null,
    val addToCartMessage: String? = null,
    val navigateToCart: Boolean = false
)