package com.shuham.ganga.domain.usecase

import com.shuham.ganga.data.local.entity.CartEntity
import com.shuham.ganga.domain.repository.CartRepository

class AddToCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(item: CartEntity) = repository.addToCart(item)
}
