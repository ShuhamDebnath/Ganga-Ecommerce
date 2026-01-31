package com.shuham.ganga.presentation.auth.onboarding

data class OnboardingState(
    val currentPage: Int = 0,
    val isLastPage: Boolean = false
)