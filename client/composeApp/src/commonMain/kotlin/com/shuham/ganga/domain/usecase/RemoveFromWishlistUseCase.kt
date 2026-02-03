package com.shuham.ganga.domain.usecase


import com.shuham.ganga.data.mapper.toWishlistEntity
import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.domain.repository.WishlistRepository



class RemoveFromWishlistUseCase(private val repository: WishlistRepository) {
    suspend operator fun invoke(product: Product) {
        repository.removeFromWishlist(product.toWishlistEntity())
    }
}