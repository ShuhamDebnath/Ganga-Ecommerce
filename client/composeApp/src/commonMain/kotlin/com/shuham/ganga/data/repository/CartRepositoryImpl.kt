package com.shuham.ganga.data.repository

import com.shuham.ganga.data.local.dao.CartDao
import com.shuham.ganga.data.local.entity.CartEntity
import com.shuham.ganga.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartEntity>> {
        return cartDao.getAllCartItems()
    }

    override fun getCartTotal(): Flow<Double?> {
        return cartDao.getTotalPrice()
    }

    override suspend fun addToCart(item: CartEntity) {
        // Check if item exists to increment quantity instead of overwriting?
        // For simple "Add", we can just insert (REPLACE strategy handles update if ID matches)
        // Ideally, we check first:
        val existing = cartDao.getCartItemById(item.productId)
        if (existing != null) {
            cartDao.updateQuantity(item.productId, existing.quantity + 1)
        } else {
            cartDao.insertCartItem(item)
        }
    }

    override suspend fun removeFromCart(item: CartEntity) {
        cartDao.deleteCartItem(item)
    }

    override suspend fun updateQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            // If quantity goes to 0, remove item? Or keep at 1?
            // Usually we remove it or just restrict UI to min 1.
            // Let's assume UI handles min 1, but safe guard here:
            val item = cartDao.getCartItemById(productId)
            if (item != null) cartDao.deleteCartItem(item)
        } else {
            cartDao.updateQuantity(productId, quantity)
        }
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}