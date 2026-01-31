package com.shuham.ganga.presentation.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class Screen(val route: String) {


    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object SignUp : Screen("signup")
}