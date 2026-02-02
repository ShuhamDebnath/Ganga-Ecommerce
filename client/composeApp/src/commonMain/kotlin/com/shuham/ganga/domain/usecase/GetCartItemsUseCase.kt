package com.shuham.ganga.domain.usecase

import com.shuham.ganga.data.local.entity.CartEntity
import com.shuham.ganga.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class GetCartItemsUseCase(private val repository: CartRepository) {
    operator fun invoke(): Flow<List<CartEntity>> = repository.getCartItems()
}