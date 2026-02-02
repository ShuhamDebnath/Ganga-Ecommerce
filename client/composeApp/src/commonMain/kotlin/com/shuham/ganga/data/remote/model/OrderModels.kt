package com.shuham.ganga.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// --- REQUEST (What we send to Backend) ---
@Serializable
data class OrderRequest(
    val orderItems: List<OrderItemDto>,
    val shippingAddress: ShippingAddressDto,
    val paymentMethod: String,
    val totalPrice: Double
)

@Serializable
data class OrderItemDto(
    @SerialName("product_id") val productId: String,
    @SerialName("vendor_id") val vendorId: String,
    val title: String,
    val image: String,
    val price: Double,
    val quantity: Int
)

@Serializable
data class ShippingAddressDto(
    val fullName: String,
    val address: String,
    val city: String,
    val postalCode: String,
    val country: String,
    val phone: String
)

// --- RESPONSE (What we get back) ---
@Serializable
data class OrderResponse(
    val success: Boolean,
    val data: OrderDataDto? = null,
    val message: String? = null
)

@Serializable
data class OrderDataDto(
    @SerialName("_id") val id: String,
    val total_price: Double,
    val is_paid: Boolean
)