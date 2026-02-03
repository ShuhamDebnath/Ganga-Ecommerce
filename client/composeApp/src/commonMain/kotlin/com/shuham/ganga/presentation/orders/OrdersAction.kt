package com.shuham.ganga.presentation.orders

sealed interface OrdersAction {
    data object OnBackClick : OrdersAction
    data class OnOrderClick(val orderId: String) : OrdersAction
    data class OnCancelClick(val orderId: String) : OrdersAction
}