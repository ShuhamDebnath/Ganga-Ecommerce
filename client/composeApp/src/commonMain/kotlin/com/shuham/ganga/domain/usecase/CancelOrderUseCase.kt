package com.shuham.ganga.domain.usecase

import com.shuham.ganga.domain.repository.OrderRepository
import com.shuham.ganga.utils.NetworkResult

class CancelOrderUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(orderId: String): NetworkResult<Boolean> {
        return repository.cancelOrder(orderId)
    }
}