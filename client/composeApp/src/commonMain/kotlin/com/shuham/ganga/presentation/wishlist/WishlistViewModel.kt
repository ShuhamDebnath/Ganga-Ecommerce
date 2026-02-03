package com.shuham.ganga.presentation.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.domain.usecase.GetWishlistUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update

class WishlistViewModel(
    private val getWishlistUseCase: GetWishlistUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WishlistState())
    val state = _state.asStateFlow()

    init {
        getWishlistUseCase().onEach { items ->
            _state.update { it.copy(items = items) }
        }.launchIn(viewModelScope)
    }
}