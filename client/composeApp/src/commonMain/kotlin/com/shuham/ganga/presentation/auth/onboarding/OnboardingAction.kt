package com.shuham.ganga.presentation.auth.onboarding

sealed interface OnboardingAction {
    data class OnPageChange(val index: Int) : OnboardingAction
    data object OnNextClick : OnboardingAction
    data object OnSkipClick : OnboardingAction
    data object OnGetStartedClick : OnboardingAction
}