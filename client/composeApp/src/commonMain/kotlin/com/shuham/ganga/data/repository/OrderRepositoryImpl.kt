package com.shuham.ganga.data.repository

import com.shuham.ganga.data.local.TokenManager
import com.shuham.ganga.data.remote.model.OrderDataDto
import com.shuham.ganga.data.remote.model.OrderListResponse
import com.shuham.ganga.data.remote.model.OrderRequest
import com.shuham.ganga.data.remote.model.OrderResponse
import com.shuham.ganga.domain.repository.OrderRepository
import com.shuham.ganga.utils.NetworkResult
import com.shuham.ganga.utils.PlatformConstants
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
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


    override suspend fun getMyOrders(): NetworkResult<List<OrderDataDto>> {
        return try {
            val token =
                tokenManager.getAccessToken() ?: return NetworkResult.Error("User not logged in")

            val response = client.get("${PlatformConstants.BASE_URL}orders/myorders") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }

            // Check for 401 ... (omitted for brevity, same logic as createOrder)

            val listResponse: OrderListResponse = response.body()
            if (listResponse.success) {
                NetworkResult.Success(listResponse.data)
            } else {
                NetworkResult.Error("Failed to load orders")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            NetworkResult.Error(e.message ?: "Network Error")
        }
    }

    override suspend fun cancelOrder(orderId: String): NetworkResult<Boolean> {
        return try {
            val token = tokenManager.getAccessToken() ?: return NetworkResult.Error("User not logged in")
            val response = client.put("${PlatformConstants.BASE_URL}orders/$orderId/cancel") {
                header(HttpHeaders.Authorization, "Bearer $token")
            }
            // Assuming successful cancel returns 200 OK
            if (response.status.value in 200..299) {
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error("Failed to cancel")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network Error")
        }
    }
}