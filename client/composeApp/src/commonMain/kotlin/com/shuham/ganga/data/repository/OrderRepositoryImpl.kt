package com.shuham.ganga.data.repository

import com.shuham.ganga.data.local.TokenManager
import com.shuham.ganga.data.remote.model.OrderRequest
import com.shuham.ganga.data.remote.model.OrderResponse
import com.shuham.ganga.domain.repository.OrderRepository
import com.shuham.ganga.utils.NetworkResult
import com.shuham.ganga.utils.PlatformConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class OrderRepositoryImpl(
    private val client: HttpClient,
    private val tokenManager: TokenManager
) : OrderRepository {

    override suspend fun createOrder(orderRequest: OrderRequest): NetworkResult<OrderResponse> {
        return try {
            val token = tokenManager.getAccessToken() ?: return NetworkResult.Error("User not logged in")

            val response = client.post("${PlatformConstants.BASE_URL}orders") {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Authorization, "Bearer $token")
                setBody(orderRequest)
            }

            // Check status code first
            if (response.status == HttpStatusCode.Unauthorized) {
                return NetworkResult.Error("Session expired. Please login again.")
            }

            try {
                val orderResponse: OrderResponse = response.body()
                if (orderResponse.success) {
                    NetworkResult.Success(orderResponse)
                } else {
                    NetworkResult.Error(orderResponse.message ?: "Order failed")
                }
            } catch (e: Exception) {
                // If parsing fails (e.g. backend sent HTML error page instead of JSON)
                NetworkResult.Error("Server Error: ${response.status.value}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.message ?: "Network Error")
        }
    }
}