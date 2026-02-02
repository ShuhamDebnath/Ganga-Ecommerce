package com.shuham.ganga.domain.repository

import com.shuham.ganga.data.remote.model.ProductDto
import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(page: Int = 1, category: String? = null, query: String? = null): Flow<NetworkResult<List<Product>>>
    suspend fun getProductById(productId: String): NetworkResult<Product>
}