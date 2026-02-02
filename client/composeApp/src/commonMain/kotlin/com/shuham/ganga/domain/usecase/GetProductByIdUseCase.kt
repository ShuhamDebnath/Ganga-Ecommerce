package com.shuham.ganga.domain.usecase

import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.utils.NetworkResult

class GetProductByIdUseCase(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(productId: String): NetworkResult<Product> {
        return repository.getProductById(productId)
    }
}