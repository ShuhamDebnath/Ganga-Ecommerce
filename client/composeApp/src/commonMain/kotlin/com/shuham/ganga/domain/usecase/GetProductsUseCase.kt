package com.shuham.ganga.domain.usecase

import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(
    private val repository: ProductRepository
) {
    operator fun invoke(page: Int = 1, category: String? = null, query: String? = null): Flow<NetworkResult<List<Product>>> {
        return repository.getProducts(page, category, query)
    }
}