package com.shuham.ganga.presentation.detail

sealed interface ProductDetailAction {
    data class LoadProduct(val id: String) : ProductDetailAction
    data object OnBackClick : ProductDetailAction
    data object OnShareClick : ProductDetailAction
    data object OnToggleWishlist : ProductDetailAction
    data object OnAddToCart : ProductDetailAction
    data object OnCheckout : ProductDetailAction
    data class OnImageChange(val index: Int) : ProductDetailAction

    data object OnMessageShown : ProductDetailAction
    data object OnNavigationHandled : ProductDetailAction
    data object OnViewCartClick : ProductDetailAction
}