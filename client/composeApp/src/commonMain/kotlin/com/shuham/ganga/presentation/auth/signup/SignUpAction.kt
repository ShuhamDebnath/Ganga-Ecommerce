package com.shuham.ganga.presentation.auth.signup

sealed interface SignUpAction {
    data class OnNameChange(val name: String) : SignUpAction
    data class OnEmailChange(val email: String) : SignUpAction
    data class OnPasswordChange(val password: String) : SignUpAction
    data class OnStoreNameChange(val storeName: String) : SignUpAction
    data class OnToggleVendor(val isVendor: Boolean) : SignUpAction
    data object OnToggleTerms : SignUpAction
    data object OnTogglePasswordVisibility : SignUpAction
    data object OnSignUpClick : SignUpAction
    data object OnClearError : SignUpAction
    data object OnGoogleSignInClick : SignUpAction


    // Navigation
    data object NavigateToLogin : SignUpAction
}