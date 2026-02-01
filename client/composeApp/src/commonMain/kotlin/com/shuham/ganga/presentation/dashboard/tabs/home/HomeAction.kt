package com.shuham.ganga.presentation.dashboard.tabs.home

import com.shuham.ganga.data.remote.model.ProductDto

sealed interface HomeAction {
    data class OnProductClick(val product: ProductDto) : HomeAction
    data object OnRefresh : HomeAction
}