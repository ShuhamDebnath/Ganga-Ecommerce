package com.shuham.ganga.presentation.auth.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shuham.ganga.data.local.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SplashViewModel(
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    private val _navigationEvent = MutableStateFlow<SplashNavigation?>(null)
    val navigationEvent = _navigationEvent.asStateFlow()

    init {
        checkStartDestination()
    }

    private fun checkStartDestination() {
        viewModelScope.launch {
            delay(500)

            val token = tokenManager.getAccessToken()
            val isFirstRun = tokenManager.isFirstRun()

            // PRIORITY FIX: Check Token FIRST
            if (!token.isNullOrBlank()) {
                // User is already logged in -> Go to Dashboard
                _navigationEvent.value = SplashNavigation.ToDashboard
            } else if (isFirstRun) {
                // No token & First Run -> Go to Onboarding
                _navigationEvent.value = SplashNavigation.ToOnboarding
            } else {
                // No token & Not First Run -> Go to Login
                _navigationEvent.value = SplashNavigation.ToAuth
            }
        }
    }
}

sealed interface SplashNavigation {
    data object ToDashboard : SplashNavigation
    data object ToOnboarding : SplashNavigation
    data object ToAuth : SplashNavigation
}