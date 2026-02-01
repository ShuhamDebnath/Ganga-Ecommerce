package com.shuham.ganga.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.shuham.ganga.presentation.dashboard.components.DashboardBottomBar
import com.shuham.ganga.presentation.dashboard.tabs.home.HomeScreenRoot
import com.shuham.ganga.presentation.dashboard.tabs.search.SearchScreenRoot
import com.shuham.ganga.presentation.detail.ProductDetailScreenRoot
import com.shuham.ganga.presentation.navigation.CartRoute
import com.shuham.ganga.presentation.navigation.HomeRoute
import com.shuham.ganga.presentation.navigation.ProductDetailRoute
import com.shuham.ganga.presentation.navigation.ProfileRoute
import com.shuham.ganga.presentation.navigation.SearchRoute


@Composable
fun DashboardScreenRoot(
) {
    val dashboardNavController = rememberNavController()

    // 1. Define Top Level Screens
    val topLevelRoutes = listOf(
        DashboardTab.Home,
        DashboardTab.Search,
        DashboardTab.Cart,
        DashboardTab.Profile
    )

    // 2. Track BackStack to toggle Bottom Bar visibility
    val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Show Bottom Bar ONLY if the current route is in the topLevelRoutes list
    val showBottomBar = topLevelRoutes.any { item ->
        currentDestination?.hierarchy?.any { it.hasRoute(item.route) } == true
    }

    val selectedTab = when {
        currentDestination?.hierarchy?.any { it.hasRoute(HomeRoute::class) } == true -> DashboardTab.Home
        currentDestination?.hierarchy?.any { it.hasRoute(SearchRoute::class) } == true -> DashboardTab.Search
        currentDestination?.hierarchy?.any { it.hasRoute(CartRoute::class) } == true -> DashboardTab.Cart
        currentDestination?.hierarchy?.any { it.hasRoute(ProfileRoute::class) } == true -> DashboardTab.Profile
        else -> DashboardTab.Home // Default
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                DashboardBottomBar(
                    selectedTab = selectedTab,
                    onTabSelected = { tab ->
                        val route = when (tab) {
                            DashboardTab.Home -> HomeRoute
                            DashboardTab.Search -> SearchRoute
                            DashboardTab.Cart -> CartRoute
                            DashboardTab.Profile -> ProfileRoute
                        }

                        dashboardNavController.navigate(route) {
                            popUpTo(dashboardNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        NavHost(
            navController = dashboardNavController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(innerPadding)
        ) {
            // --- TAB 1: HOME ---
            composable<HomeRoute> {
                HomeScreenRoot(
                    onNavigateToProduct = {
                        dashboardNavController.navigate(ProductDetailRoute(it))
                    }
                )
            }

            // --- TAB 2: SEARCH ---
            composable<SearchRoute> {
                SearchScreenRoot(
                    onNavigateToProduct = {
                        dashboardNavController.navigate(ProductDetailRoute(it))
                    },
                    onNavigateBack = { /* No-op or handle specific logic */ }
                )
            }

            // --- TAB 3: CART ---
            composable<CartRoute> {
                PlaceholderScreen("Cart")
            }

            // --- TAB 4: PROFILE ---
            composable<ProfileRoute> {
                PlaceholderScreen("Profile")
            }

            // 5. PRODUCT DETAILS (Standalone)
            composable<ProductDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<ProductDetailRoute>()
                ProductDetailScreenRoot(
                    productId = route.id,
                    onNavigateBack = { dashboardNavController.popBackStack() }
                )
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Coming Soon: $name", color = androidx.compose.ui.graphics.Color.Gray)
    }
}