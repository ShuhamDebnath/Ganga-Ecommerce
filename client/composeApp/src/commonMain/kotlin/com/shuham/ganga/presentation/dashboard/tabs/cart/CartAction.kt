package com.shuham.ganga.presentation.dashboard.tabs.cart

sealed interface CartAction {
    data class OnUpdateQuantity(val itemId: String, val newQuantity: Int) : CartAction
    data object OnCheckoutClick : CartAction
    data object OnBackClick : CartAction
}