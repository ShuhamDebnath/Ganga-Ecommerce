package com.shuham.ganga.presentation.auth.signup

data class SignUpState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isVendor: Boolean = false,
    val storeName: String = "",
    val agreedToTerms: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)