package com.shuham.ganga.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.serialization.Serializable

@Serializable
object SplashRoute
@Serializable
object OnboardingRoute

// Graphs
@Serializable
object AuthGraph
@Serializable
object DashboardGraph

// Auth Routes
@Serializable
object LoginRoute
@Serializable
object SignUpRoute

// Dashboard Routes
@Serializable
object HomeRoute
@Serializable
object SearchRoute
@Serializable
object CartRoute
@Serializable
object ProfileRoute

@Serializable
data class ProductDetailRoute(val id: String)

// --- Helper for Development ---
@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "TODO: $name")
    }
}