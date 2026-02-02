package com.shuham.ganga.data.repository

import com.shuham.ganga.data.local.dao.ProductDao
import com.shuham.ganga.data.mapper.toDomain
import com.shuham.ganga.data.mapper.toEntity
import com.shuham.ganga.data.remote.model.ProductResponse
import com.shuham.ganga.data.remote.model.SingleProductResponse
import com.shuham.ganga.domain.model.Product
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.utils.NetworkResult
import com.shuham.ganga.utils.PlatformConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductRepositoryImpl(
    private val client: HttpClient,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(page: Int, category: String?, query: String?): Flow<NetworkResult<List<Product>>> = flow {
        emit(NetworkResult.Loading())

        // 1. Cache Check
        // If searching, we check the DB with the query. If just browsing, we get all.
        val dbQuery = query ?: ""
        val localData = productDao.searchProducts(dbQuery)

        if (localData.isNotEmpty()) {
            emit(NetworkResult.Success(localData.map { it.toDomain() }))
        }

        // 2. Network Fetch
        try {
            val response = client.get("${PlatformConstants.BASE_URL}products") {
                parameter("page", page)
                if (category != null) parameter("category", category)
                if (query != null) parameter("keyword", query)
            }
            val productResponse: ProductResponse = response.body()

            if (productResponse.success) {
                // 3. Update Cache
                val entities = productResponse.data.map { it.toEntity() }
                // Only clear if we are on page 1 and no search query (refreshing main feed)
                if (page == 1 && query == null && category == null) {
                    productDao.clearProducts()
                }
                productDao.insertProducts(entities)

                // 4. Emit Fresh Data
                emit(NetworkResult.Success(entities.map { it.toDomain() }))
            } else {
                emit(NetworkResult.Error("Server Error", localData.map { it.toDomain() }))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emit(NetworkResult.Error("Offline: ${e.message}", localData.map { it.toDomain() }))
        }
    }

    override suspend fun getProductById(productId: String): NetworkResult<Product> {
        return try {
            val response = client.get("${PlatformConstants.BASE_URL}products/$productId")
            val singleResponse: SingleProductResponse = response.body()

            if (singleResponse.success && singleResponse.data != null) {
                val dto = singleResponse.data
                productDao.insertProducts(listOf(dto.toEntity()))
                NetworkResult.Success(dto.toDomain())
            } else {
                val local = productDao.getProductById(productId)
                if (local != null) NetworkResult.Success(local.toDomain()) else NetworkResult.Error("Product not found")
            }
        } catch (e: Exception) {
            val local = productDao.getProductById(productId)
            if (local != null) NetworkResult.Success(local.toDomain()) else NetworkResult.Error("Network error: ${e.message}")
        }
    }
}