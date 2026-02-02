package com.shuham.ganga.domain.repository

import com.shuham.ganga.data.local.entity.CartEntity
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartEntity>>
    fun getCartTotal(): Flow<Double?>
    suspend fun addToCart(item: CartEntity)
    suspend fun removeFromCart(item: CartEntity)
    suspend fun updateQuantity(productId: String, quantity: Int)
    suspend fun clearCart()
}