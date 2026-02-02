package com.shuham.ganga.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.data.remote.model.OrderItemDto
import com.shuham.ganga.data.remote.model.OrderRequest
import com.shuham.ganga.data.remote.model.ShippingAddressDto
import com.shuham.ganga.domain.usecase.ClearCartUseCase
import com.shuham.ganga.domain.usecase.GetCartItemsUseCase
import com.shuham.ganga.domain.usecase.PlaceOrderUseCase
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val placeOrderUseCase: PlaceOrderUseCase,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state = _state.asStateFlow()

    init {
        loadOrderSummary()
    }

    private fun loadOrderSummary() {
        _state.update { it.copy(isLoading = true) }

        getCartItemsUseCase()
            .onEach { items ->
                val subtotal = items.sumOf { it.price * it.quantity }
                val tax = subtotal * 0.05
                val shipping = if (subtotal > 1000) 0.0 else 50.0
                val total = subtotal + tax + shipping

                _state.update {
                    it.copy(
                        isLoading = false,
                        items = items,
                        subtotal = subtotal,
                        tax = tax,
                        shippingFee = shipping,
                        totalAmount = total
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CheckoutAction) {
        when (action) {
            is CheckoutAction.OnPaymentMethodSelect -> {
                _state.update { it.copy(selectedPaymentMethod = action.method) }
            }
            CheckoutAction.OnPlaceOrderClick -> placeOrder()
            else -> Unit
        }
    }

    private fun placeOrder() {
        val currentState = _state.value
        if (currentState.items.isEmpty()) return

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            // 1. Map Data
            val orderItems = currentState.items.map { item ->
                OrderItemDto(
                    productId = item.productId,
                    vendorId = item.vendorId,
                    title = item.title,
                    image = item.imageUrl,
                    price = item.price,
                    quantity = item.quantity
                )
            }

            val shippingAddress = ShippingAddressDto(
                fullName = "John Doe",
                address = "123 Market Road, Indiranagar",
                city = "Bangalore",
                postalCode = "560038",
                country = "India",
                phone = "+91 9876543210"
            )

            val request = OrderRequest(
                orderItems = orderItems,
                shippingAddress = shippingAddress,
                paymentMethod = currentState.selectedPaymentMethod.name,
                totalPrice = currentState.totalAmount
            )

            // 2. Call UseCase
            val result = placeOrderUseCase(request)

            when (result) {
                is NetworkResult.Success -> {
                    // 3. Clear Cart via UseCase
                    clearCartUseCase()
                    _state.update { it.copy(isLoading = false, isOrderPlaced = true) }
                }
                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, error = result.message ?: "Order Failed") }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}