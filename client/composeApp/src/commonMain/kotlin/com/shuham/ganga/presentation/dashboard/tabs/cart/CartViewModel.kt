package com.shuham.ganga.presentation.dashboard.tabs.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.repository.CartRepository
import com.shuham.ganga.domain.usecase.GetCartItemsUseCase
import com.shuham.ganga.domain.usecase.UpdateCartQuantityUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartViewModel(
    private val getCartItemsUseCase: GetCartItemsUseCase,
    private val updateCartQuantityUseCase: UpdateCartQuantityUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CartState())
    val state = _state.asStateFlow()

    init {
        loadCart()
    }

    private fun loadCart() {
        _state.update { it.copy(isLoading = true) }

        // UseCase returns a Flow
        getCartItemsUseCase()
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
                    updateCartQuantityUseCase(action.itemId, action.newQuantity)
                }
            }
            CartAction.OnCheckoutClick -> {
                // Navigation handled by UI
            }
            CartAction.OnBackClick -> { }
        }
    }
}