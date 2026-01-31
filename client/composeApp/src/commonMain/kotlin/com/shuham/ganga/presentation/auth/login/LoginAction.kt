package com.shuham.ganga.presentation.auth.login

sealed interface LoginAction {
    data class OnEmailChange(val email: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data object OnTogglePasswordVisibility : LoginAction
    data object OnLoginClick : LoginAction
    data object OnSignUpClick : LoginAction
    data object OnForgotPasswordClick : LoginAction
    data object OnGoogleSignInClick : LoginAction
    data object ClearError : LoginAction
}