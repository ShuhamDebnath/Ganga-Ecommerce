package com.shuham.ganga.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.data.local.entity.CartEntity
import com.shuham.ganga.domain.repository.CartRepository
import com.shuham.ganga.domain.repository.ProductRepository
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProductDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: ProductDetailAction) {
        when (action) {
            is ProductDetailAction.LoadProduct -> loadProduct(action.id)
            is ProductDetailAction.OnImageChange -> {
                _state.update { it.copy(selectedImageIndex = action.index) }
            }

            ProductDetailAction.OnAddToCart -> addToCart(navigateAfter = false)
            ProductDetailAction.OnCheckout -> addToCart(navigateAfter = true)
            ProductDetailAction.OnMessageShown -> {
                _state.update { it.copy(addToCartMessage = null) }
            }

            ProductDetailAction.OnNavigationHandled -> {
                _state.update { it.copy(navigateToCart = false) }
            }

            ProductDetailAction.OnViewCartClick -> {
                _state.update { it.copy(navigateToCart = true) }
            }

            else -> {}
        }
    }

    private fun loadProduct(id: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.getProductById(id)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update { it.copy(isLoading = false, product = result.data) }
                }

                is NetworkResult.Error -> {
                    _state.update { it.copy(isLoading = false, errorMessage = result.message) }
                }

                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun addToCart(navigateAfter: Boolean) {
        val product = _state.value.product ?: return

        viewModelScope.launch {
            val cartItem = CartEntity(
                productId = product.id,
                vendorId = product.vendorId ?: "Unknown Vendor",
                title = product.title,
                price = product.price,
                imageUrl = product.images.firstOrNull() ?: "",
                quantity = 1,
                category = product.category
            )

            cartRepository.addToCart(cartItem)

            _state.update {
                it.copy(
                    addToCartMessage = if (!navigateAfter) "Added to Cart" else null,
                    navigateToCart = navigateAfter
                )
            }
        }
    }
}