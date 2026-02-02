package com.shuham.ganga.domain.repository

import com.shuham.ganga.data.remote.model.OrderRequest
import com.shuham.ganga.data.remote.model.OrderResponse
import com.shuham.ganga.utils.NetworkResult

interface OrderRepository {
    suspend fun createOrder(orderRequest: OrderRequest): NetworkResult<OrderResponse>
}