package com.shuham.ganga.presentation.checkout

sealed interface CheckoutAction {
    data object OnBackClick : CheckoutAction
    data class OnPaymentMethodSelect(val method: PaymentMethod) : CheckoutAction
    data object OnPlaceOrderClick : CheckoutAction
}