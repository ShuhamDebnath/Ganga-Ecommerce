package com.shuham.ganga

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.shuham.ganga.presentation.auth.login.LoginScreenRoot
import com.shuham.ganga.presentation.auth.onboarding.OnboardingScreenRoot
import com.shuham.ganga.presentation.auth.signup.SignUpScreenRoot
import com.shuham.ganga.presentation.auth.splash.SplashScreenRoot
import com.shuham.ganga.presentation.dashboard.DashboardScreenRoot
import com.shuham.ganga.presentation.navigation.AuthGraph
import com.shuham.ganga.presentation.navigation.CartRoute
import com.shuham.ganga.presentation.navigation.DashboardGraph
import com.shuham.ganga.presentation.navigation.HomeRoute
import com.shuham.ganga.presentation.navigation.LoginRoute
import com.shuham.ganga.presentation.navigation.OnboardingRoute
import com.shuham.ganga.presentation.navigation.PlaceholderScreen
import com.shuham.ganga.presentation.navigation.ProfileRoute
import com.shuham.ganga.presentation.navigation.SearchRoute
import com.shuham.ganga.presentation.navigation.SignUpRoute
import com.shuham.ganga.presentation.navigation.SplashRoute
import com.shuham.ganga.presentation.theme.GangaTheme

@Composable
@Preview
fun App() {
//    MaterialTheme {
    GangaTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = SplashRoute
        ) {
            // 1. Splash Route
            composable<SplashRoute> {
                SplashScreenRoot(
                    navigateToDashboard = {
                        navController.navigate(DashboardGraph) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    },
                    navigateToOnboarding = {
                        navController.navigate(OnboardingRoute) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    },
                    navigateToAuth = {
                        navController.navigate(AuthGraph) {
                            popUpTo(SplashRoute) { inclusive = true }
                        }
                    }
                )
            }

            // 2. Onboarding Route
            composable<OnboardingRoute> {
                OnboardingScreenRoot(
                    onFinished = {
                        navController.navigate(AuthGraph) {
                            popUpTo(OnboardingRoute) { inclusive = true }
                        }
                    }
                )
            }

            // 3. Auth Nested Graph
            navigation<AuthGraph>(startDestination = LoginRoute) {
                composable<LoginRoute> {
                    LoginScreenRoot(
                        onLoginSuccess = {
                            navController.navigate(DashboardGraph) {
                                popUpTo(AuthGraph) { inclusive = true }
                            }
                        },
                        onNavigateToSignUp = {
                            navController.navigate(SignUpRoute) {
                                popUpTo(AuthGraph) { inclusive = true }
                            }
                        }
                    )
                }

                composable<SignUpRoute> {
                    SignUpScreenRoot(
                        onNavigateToLogin = {
                            navController.navigate(LoginRoute) {
                                popUpTo(AuthGraph) { inclusive = true }
                            }
                        },
                        onSignUpSuccess = {
                            navController.navigate(AuthGraph) {
                                popUpTo(AuthGraph) { inclusive = true }
                            }
                        }


                    )
                }
            }

            // 4. Dashboard Nested Graph (Future Phase)
            navigation<DashboardGraph>(startDestination = HomeRoute) {
                composable<HomeRoute> {
                    DashboardScreenRoot(
                        onNavigateToProduct = { /* Navigate to Details */ }
                    )
                }
                composable<SearchRoute> {
                    PlaceholderScreen("Search Screen")
                }
                composable<CartRoute> {
                    PlaceholderScreen("Cart Screen")
                }
                composable<ProfileRoute> {
                    PlaceholderScreen("Profile Screen")
                }
            }
        }
    }

//    }
}