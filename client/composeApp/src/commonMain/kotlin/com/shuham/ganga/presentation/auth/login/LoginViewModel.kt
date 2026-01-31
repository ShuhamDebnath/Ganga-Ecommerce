package com.shuham.ganga.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.data.remote.model.LoginRequest
import com.shuham.ganga.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnEmailChange -> {
                _state.update { it.copy(email = action.email, errorMessage = null) }
            }
            is LoginAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password, errorMessage = null) }
            }
            LoginAction.OnTogglePasswordVisibility -> {
                _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            }
            LoginAction.OnLoginClick -> {
                login()
            }
            LoginAction.ClearError -> {
                _state.update { it.copy(errorMessage = null) }
            }
            LoginAction.OnForgotPasswordClick -> {
                // Future: Navigate to Forgot Password or trigger API
            }
            LoginAction.OnSignUpClick -> {
                // Navigation logic usually handled by NavController in UI
            }
            LoginAction.OnGoogleSignInClick -> {
                // Future: Trigger Social Auth flow
            }
        }
    }

    private fun login() {
        println("Login clicked")
        val currentEmail = _state.value.email
        val currentPassword = _state.value.password

        if (currentEmail.isBlank() || currentPassword.isBlank()) {
            _state.update { it.copy(errorMessage = "Please fill in all fields") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val result = repository.login(LoginRequest(currentEmail, currentPassword))
            println("Login result: $result")

            result.onSuccess { response ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        successMessage = "Welcome back, ${response.data?.name}!"
                    )
                }
            }.onFailure { error ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Login failed. Please try again."
                    )
                }
            }
        }
    }
}