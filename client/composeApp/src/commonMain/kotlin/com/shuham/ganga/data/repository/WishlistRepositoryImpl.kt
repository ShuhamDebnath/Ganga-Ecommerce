package com.shuham.ganga.data.repository

import com.shuham.ganga.data.local.dao.WishlistDao
import com.shuham.ganga.data.local.entity.WishlistEntity
import com.shuham.ganga.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow

class WishlistRepositoryImpl(
    private val dao: WishlistDao
) : WishlistRepository {
    override fun getWishlist(): Flow<List<WishlistEntity>> = dao.getWishlist()
    override suspend fun addToWishlist(item: WishlistEntity) = dao.addToWishlist(item)
    override suspend fun removeFromWishlist(item: WishlistEntity) = dao.removeFromWishlist(item)
    override fun isProductInWishlist(productId: String): Flow<Boolean> = dao.isProductInWishlist(productId)
}