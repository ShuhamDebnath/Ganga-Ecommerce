package com.shuham.ganga.presentation.dashboard.tabs.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        _state.update { it.copy(isLoading = true) }

        repository.getCartItems()
            .onEach { items ->
                val total = items.sumOf { it.price * it.quantity }
                _state.update {
                    it.copy(
                        isLoading = false,
                        cartItems = items,
                        totalPrice = total
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: CartAction) {
        when (action) {
            is CartAction.OnUpdateQuantity -> {
                viewModelScope.launch {
                    repository.updateQuantity(action.itemId, action.newQuantity)
                }
            }
            CartAction.OnCheckoutClick -> {

            }
            CartAction.OnBackClick -> { }
        }
    }
}