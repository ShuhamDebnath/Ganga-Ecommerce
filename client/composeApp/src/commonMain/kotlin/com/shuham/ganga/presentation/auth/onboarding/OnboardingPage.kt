package com.shuham.ganga.presentation.auth.onboarding

import org.jetbrains.compose.resources.DrawableResource

data class OnboardingPage(
    val title: String,
    val description: String,
    val image: DrawableResource,
    val icon: DrawableResource
)