package com.shuham.ganga.domain.usecase

import com.shuham.ganga.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow

class IsProductInWishlistUseCase(private val repository: WishlistRepository) {
    operator fun invoke(productId: String): Flow<Boolean> = repository.isProductInWishlist(productId)
}