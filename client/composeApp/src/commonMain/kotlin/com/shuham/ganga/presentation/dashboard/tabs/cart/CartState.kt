package com.shuham.ganga.presentation.dashboard.tabs.cart

import com.shuham.ganga.data.local.entity.CartEntity

data class CartState(
    val isLoading: Boolean = false,
    val cartItems: List<CartEntity> = emptyList(),
    val totalPrice: Double = 0.0
) {
    val groupedItems: Map<String, List<CartEntity>>
        get() = cartItems.groupBy { it.vendorId }
}