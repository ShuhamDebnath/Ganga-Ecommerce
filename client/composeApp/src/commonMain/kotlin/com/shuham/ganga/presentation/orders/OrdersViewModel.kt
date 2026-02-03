package com.shuham.ganga.presentation.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.usecase.CancelOrderUseCase
import com.shuham.ganga.domain.usecase.GetOrdersUseCase
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrdersViewModel(
    private val getOrdersUseCase: GetOrdersUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(OrdersState())
    val state = _state.asStateFlow()

    init {
        loadOrders()
    }

    private fun loadOrders() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = getOrdersUseCase()
            when (result) {
                is NetworkResult.Success -> _state.update { it.copy(isLoading = false, orders = result.data ?: emptyList()) }
                is NetworkResult.Error -> _state.update { it.copy(isLoading = false, error = result.message) }
                is NetworkResult.Loading -> _state.update { it.copy(isLoading = true) }
            }
        }
    }

    fun onAction(action: OrdersAction) {
        when (action) {
            is OrdersAction.OnCancelClick -> cancelOrder(action.orderId)
            else -> Unit
        }
    }

    private fun cancelOrder(orderId: String) {
        viewModelScope.launch {
            val result = cancelOrderUseCase(orderId)
            if (result is NetworkResult.Success) {
                loadOrders() // Refresh list
            }
        }
    }
}