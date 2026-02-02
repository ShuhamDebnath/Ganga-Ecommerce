package com.shuham.ganga.domain.usecase

import com.shuham.ganga.data.local.entity.CartEntity
import com.shuham.ganga.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow


class UpdateCartQuantityUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(itemId: String, quantity: Int) = repository.updateQuantity(itemId, quantity)
}