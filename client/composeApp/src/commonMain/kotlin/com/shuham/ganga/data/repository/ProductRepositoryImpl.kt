package com.shuham.ganga.data.repository

import com.shuham.ganga.data.remote.model.ProductDto
import com.shuham.ganga.data.remote.model.ProductResponse
import com.shuham.ganga.data.remote.model.SingleProductResponse
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.utils.Constants
import com.shuham.ganga.utils.NetworkResult
import com.shuham.ganga.utils.PlatformConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class ProductRepositoryImpl(
    private val client: HttpClient
) : ProductRepository {

    private fun getBaseUrl(): String {
        return PlatformConstants.BASE_URL
    }

    override suspend fun getProducts(page: Int, category: String?): NetworkResult<List<ProductDto>> {
        return try {
            val response = client.get("${getBaseUrl()}products") {
                parameter("page", page)
                if (category != null) parameter("category", category)
            }
            val productResponse: ProductResponse = response.body()

            if (productResponse.success) {
                NetworkResult.Success(productResponse.data)
            } else {
                NetworkResult.Error("Failed to load products")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.message ?: "Unknown Error")
        }
    }

    override suspend fun getProductById(productId: String): NetworkResult<ProductDto> {
        return try {
            val response = client.get("${PlatformConstants.BASE_URL}products/$productId")
            val singleResponse: SingleProductResponse = response.body()

            if (singleResponse.success && singleResponse.data != null) {
                NetworkResult.Success(singleResponse.data)
            } else {
                NetworkResult.Error("Product not found")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.message ?: "Network Error")
        }
    }
}