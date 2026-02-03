package com.shuham.ganga.domain.usecase

import com.shuham.ganga.data.remote.model.OrderDataDto
import com.shuham.ganga.domain.repository.OrderRepository
import com.shuham.ganga.utils.NetworkResult

class GetOrdersUseCase(private val repository: OrderRepository) {
    suspend operator fun invoke(): NetworkResult<List<OrderDataDto>> {
        return repository.getMyOrders()
    }
}