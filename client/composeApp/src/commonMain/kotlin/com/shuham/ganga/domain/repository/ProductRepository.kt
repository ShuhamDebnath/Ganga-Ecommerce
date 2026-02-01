package com.shuham.ganga.domain.repository

import com.shuham.ganga.data.remote.model.ProductDto
import com.shuham.ganga.utils.NetworkResult

interface ProductRepository {
    suspend fun getProducts(page: Int = 1, category: String? = null): NetworkResult<List<ProductDto>>
    suspend fun getProductById(productId: String): NetworkResult<ProductDto>
}