package com.shuham.ganga.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.data.remote.model.OrderItemDto
import com.shuham.ganga.data.remote.model.OrderRequest
import com.shuham.ganga.data.remote.model.ShippingAddressDto
import com.shuham.ganga.domain.repository.CartRepository
import com.shuham.ganga.domain.repository.OrderRepository
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CheckoutViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CheckoutState())
    val state = _state.asStateFlow()

    init {
        loadOrderSummary()
    }

    private fun loadOrderSummary() {
        _state.update { it.copy(isLoading = true) }

        cartRepository.getCartItems()
            .onEach { items ->
                val subtotal = items.sumOf { it.price * it.quantity }
                val tax = subtotal * 0.05 // 5% GST
                val shipping = if (subtotal > 1000) 0.0 else 50.0 // Free shipping over 1000
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

            // 1. Map Local Cart Entity -> Network DTO
            val orderItems = currentState.items.map { item ->
                OrderItemDto(
                    productId = item.productId,
                    vendorId = item.vendorId, // Required for backend order splitting
                    title = item.title,
                    image = item.imageUrl,
                    price = item.price,
                    quantity = item.quantity
                )
            }

            // 2. Create Shipping Address
            // Note: In a real app, this comes from the user's selected address profile
            val shippingAddress = ShippingAddressDto(
                fullName = "John Doe",
                address = "123 Market Road, Indiranagar",
                city = "Bangalore",
                postalCode = "560038",
                country = "India",
                phone = "+91 9876543210"
            )

            // 3. Build Request
            val request = OrderRequest(
                orderItems = orderItems,
                shippingAddress = shippingAddress,
                paymentMethod = currentState.selectedPaymentMethod.name,
                totalPrice = currentState.totalAmount
            )

            // 4. Send to Backend
            val result = orderRepository.createOrder(request)

            when (result) {
                is NetworkResult.Success -> {
                    // Success: Clear the cart locally since order is saved on server
                    cartRepository.clearCart()
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