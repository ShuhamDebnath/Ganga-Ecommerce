package com.shuham.ganga.presentation.wishlist

import com.shuham.ganga.domain.model.Product

data class WishlistState(val items: List<Product> = emptyList())