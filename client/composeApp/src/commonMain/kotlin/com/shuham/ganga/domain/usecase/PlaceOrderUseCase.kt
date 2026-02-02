package com.shuham.ganga.domain.usecase

import com.shuham.ganga.data.remote.model.OrderRequest
import com.shuham.ganga.data.remote.model.OrderResponse
import com.shuham.ganga.domain.repository.OrderRepository
import com.shuham.ganga.utils.NetworkResult

class PlaceOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(request: OrderRequest): NetworkResult<OrderResponse> {
        return repository.createOrder(request)
    }
}