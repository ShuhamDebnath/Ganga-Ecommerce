package com.shuham.ganga.presentation.orders

import com.shuham.ganga.data.remote.model.OrderDataDto

data class OrdersState(
    val isLoading: Boolean = false,
    val orders: List<OrderDataDto> = emptyList(),
    val error: String? = null
)