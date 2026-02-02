package com.shuham.ganga.presentation.dashboard.tabs.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.data.local.TokenManager
import com.shuham.ganga.domain.usecase.ClearCartUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val tokenManager: TokenManager,
    private val clearCartUseCase: ClearCartUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        val name = tokenManager.getUserName() ?: "User"
        // In a real app, you'd fetch email and vendor status from a user repository
        _state.update { it.copy(
            userName = name,
            userEmail = "albert.flores@example.com", // Mock for UI match
            appVersion = "App Version 2.4.0"
        ) }
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            ProfileAction.OnLogoutClick -> logout()
            ProfileAction.OnOrdersClick -> { /* Navigate */ }
            ProfileAction.OnWishlistClick -> { /* Navigate */ }
            ProfileAction.OnAddressClick -> { /* Navigate */ }
            ProfileAction.OnPaymentMethodsClick -> { /* Navigate */ }
            ProfileAction.OnHelpClick -> { /* Navigate */ }
            ProfileAction.OnPrivacyPolicyClick -> { /* Navigate */ }
            ProfileAction.OnSwitchToVendorClick -> { /* Navigate/Toggle */ }
            ProfileAction.OnEditProfileClick -> { /* Navigate */ }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            // 1. Clear Local Data
            tokenManager.clearAuth()
            clearCartUseCase()

            // 2. Navigation handled by UI observing token state or callback
        }
    }
}