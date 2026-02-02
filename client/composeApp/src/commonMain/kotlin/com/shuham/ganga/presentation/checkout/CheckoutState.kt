package com.shuham.ganga.presentation.checkout

import com.shuham.ganga.data.local.entity.CartEntity

data class CheckoutState(
    val isLoading: Boolean = false,
    val items: List<CartEntity> = emptyList(),
    val subtotal: Double = 0.0,
    val shippingFee: Double = 50.0, // Hardcoded for now
    val tax: Double = 0.0,
    val totalAmount: Double = 0.0,
    val selectedPaymentMethod: PaymentMethod = PaymentMethod.UPI,
    val isOrderPlaced: Boolean = false,
    val error: String? = null
)

enum class PaymentMethod(val title: String) {
    UPI("UPI (GPay / PhonePe)"),
    CARD("Credit / Debit Card"),
    COD("Cash on Delivery")
}