package com.shuham.ganga.presentation.auth.onboarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OnboardingViewModel : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    // We hardcode the page count here to match the UI content
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
            OnboardingAction.OnSkipClick -> {
                // Future: Save "onboarding_completed" preference here
            }
            OnboardingAction.OnGetStartedClick -> {
                // Future: Save "onboarding_completed" preference here
            }
        }
    }
}