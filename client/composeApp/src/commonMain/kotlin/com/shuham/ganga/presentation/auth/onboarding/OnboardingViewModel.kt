package com.shuham.ganga.presentation.auth.onboarding

import androidx.lifecycle.ViewModel
import com.shuham.ganga.data.local.TokenManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OnboardingViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val totalPages = 3

    fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnPageChange -> {
                _state.update {
                    it.copy(
                        currentPage = action.index,
                        isLastPage = action.index == totalPages - 1
                    )
                }
            }
            OnboardingAction.OnNextClick -> {
                if (_state.value.currentPage < totalPages - 1) {
                    _state.update {
                        val newPage = it.currentPage + 1
                        it.copy(
                            currentPage = newPage,
                            isLastPage = newPage == totalPages - 1
                        )
                    }
                }
            }
            // FIX: Handle BOTH Skip and Get Started
            OnboardingAction.OnGetStartedClick -> completeOnboarding()
        }
    }

    private fun completeOnboarding() {
        tokenManager.setFirstRunCompleted()
    }
}