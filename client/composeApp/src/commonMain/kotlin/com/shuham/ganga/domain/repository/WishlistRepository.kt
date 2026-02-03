package com.shuham.ganga.domain.repository

import com.shuham.ganga.data.local.entity.WishlistEntity
import kotlinx.coroutines.flow.Flow

interface WishlistRepository {
    fun getWishlist(): Flow<List<WishlistEntity>>
    suspend fun addToWishlist(item: WishlistEntity)
    suspend fun removeFromWishlist(item: WishlistEntity)

    fun isProductInWishlist(productId: String): Flow<Boolean>
}