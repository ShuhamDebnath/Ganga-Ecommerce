package com.shuham.ganga.presentation.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.data.remote.model.RegisterRequest
import com.shuham.ganga.domain.repository.AuthRepository
import com.shuham.ganga.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    fun onAction(action: SignUpAction) {
        when (action) {
            is SignUpAction.OnNameChange -> _state.update { it.copy(name = action.name) }
            is SignUpAction.OnEmailChange -> _state.update { it.copy(email = action.email) }
            is SignUpAction.OnPasswordChange -> _state.update { it.copy(password = action.password) }
            is SignUpAction.OnStoreNameChange -> _state.update { it.copy(storeName = action.storeName) }
            is SignUpAction.OnToggleVendor -> _state.update { it.copy(isVendor = action.isVendor) }
            SignUpAction.OnToggleTerms -> _state.update { it.copy(agreedToTerms = !it.agreedToTerms) }
            SignUpAction.OnTogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            SignUpAction.OnSignUpClick -> signUp()
            SignUpAction.OnClearError -> _state.update { it.copy(errorMessage = null) }
            SignUpAction.NavigateToLogin -> {}
            SignUpAction.OnGoogleSignInClick -> {}
        }
    }

    private fun signUp() {
        val currentState = _state.value

        // Basic Validation
        if (currentState.name.isBlank() || currentState.email.isBlank() || currentState.password.isBlank()) {
            _state.update { it.copy(errorMessage = "Please fill all fields") }
            return
        }

        if (currentState.isVendor && currentState.storeName.isBlank()) {
            _state.update { it.copy(errorMessage = "Store name is required for vendors") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }

            val role = if (currentState.isVendor) "vendor" else "customer"

            val registerRequest = RegisterRequest(
                name = currentState.name,
                email = currentState.email,
                password = currentState.password,
                role = role,
                storeName = if (currentState.isVendor) currentState.storeName else null
            )
            val result = repository.register(registerRequest)

            when (result) {
                is NetworkResult.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Account created successfully!"
                        )
                    }
                }
                is NetworkResult.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.message
                        )
                    }
                }
                is NetworkResult.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }
            }
        }
    }
}