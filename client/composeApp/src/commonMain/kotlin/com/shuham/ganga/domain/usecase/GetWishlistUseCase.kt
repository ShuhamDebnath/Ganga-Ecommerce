package com.shuham.ganga.domain.usecase

import com.shuham.ganga.data.mapper.toDomain
import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.domain.repository.WishlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetWishlistUseCase(private val repository: WishlistRepository) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getWishlist().map { list ->
            list.map { it.toDomain() }
        }
    }
}
