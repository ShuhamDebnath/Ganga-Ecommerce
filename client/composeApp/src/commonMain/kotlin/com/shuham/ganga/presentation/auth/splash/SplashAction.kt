package com.shuham.ganga.presentation.auth.splash

sealed interface SplashAction {
    data object OnAnimationFinished : SplashAction
}