package com.shuham.ganga.presentation.dashboard.tabs.profile

sealed interface ProfileAction {
    data object OnOrdersClick : ProfileAction
    data object OnWishlistClick : ProfileAction // Added
    data object OnAddressClick : ProfileAction
    data object OnPaymentMethodsClick : ProfileAction // Added
    data object OnHelpClick : ProfileAction
    data object OnPrivacyPolicyClick : ProfileAction // Added
    data object OnLogoutClick : ProfileAction
    data object OnSwitchToVendorClick : ProfileAction // Added
    data object OnEditProfileClick : ProfileAction // Added
}