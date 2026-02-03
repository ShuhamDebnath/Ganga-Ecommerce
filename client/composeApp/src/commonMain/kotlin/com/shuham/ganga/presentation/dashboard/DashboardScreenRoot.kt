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
import com.shuham.ganga.presentation.checkout.CheckoutScreenRoot
import com.shuham.ganga.presentation.dashboard.components.DashboardBottomBar
import com.shuham.ganga.presentation.dashboard.tabs.cart.CartScreenRoot
import com.shuham.ganga.presentation.dashboard.tabs.home.HomeScreenRoot
import com.shuham.ganga.presentation.dashboard.tabs.profile.ProfileScreenRoot
import com.shuham.ganga.presentation.dashboard.tabs.search.SearchScreenRoot
import com.shuham.ganga.presentation.detail.ProductDetailScreenRoot
import com.shuham.ganga.presentation.navigation.CartRoute
import com.shuham.ganga.presentation.navigation.CheckoutRoute
import com.shuham.ganga.presentation.navigation.HomeRoute
import com.shuham.ganga.presentation.navigation.OrdersRoute
import com.shuham.ganga.presentation.navigation.ProductDetailRoute
import com.shuham.ganga.presentation.navigation.ProfileRoute
import com.shuham.ganga.presentation.navigation.SearchRoute
import com.shuham.ganga.presentation.navigation.WishlistRoute
import com.shuham.ganga.presentation.orders.OrdersScreenRoot
import com.shuham.ganga.presentation.wishlist.WishlistScreenRoot


@Composable
fun DashboardScreenRoot(
    onLogout: () -> Unit
) {

    // 1. Internal NavController for the entire Consumer App Flow
    val dashboardNavController = rememberNavController()

    // 2. Define the Tabs
    val topLevelRoutes = listOf(
        DashboardTab.Home,
        DashboardTab.Search,
        DashboardTab.Cart,
        DashboardTab.Profile
    )

    // 3. Track Current Route to toggle Bottom Bar
    val navBackStackEntry by dashboardNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Logic: Only show Bottom Bar if the current screen is one of the 4 tabs.
    val showBottomBar = topLevelRoutes.any { item ->
        currentDestination?.hierarchy?.any { it.hasRoute(item.route) } == true
    }

    // Determine visual selection state
    val selectedTab = when {
        currentDestination?.hierarchy?.any { it.hasRoute(HomeRoute::class) } == true -> DashboardTab.Home
        currentDestination?.hierarchy?.any { it.hasRoute(SearchRoute::class) } == true -> DashboardTab.Search
        currentDestination?.hierarchy?.any { it.hasRoute(CartRoute::class) } == true -> DashboardTab.Cart
        currentDestination?.hierarchy?.any { it.hasRoute(ProfileRoute::class) } == true -> DashboardTab.Profile
        else -> DashboardTab.Home
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

        // 4. THE CONSUMER APP GRAPH
        // All screens (Tabs + Details + Checkout) live here now.
        NavHost(
            navController = dashboardNavController,
            startDestination = HomeRoute,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // --- TAB 1: HOME ---
            composable<HomeRoute> {
                HomeScreenRoot(
                    onNavigateToProduct = { productId ->
                        dashboardNavController.navigate(ProductDetailRoute(productId))
                    }
                )
            }

            // --- TAB 2: SEARCH ---
            composable<SearchRoute> {
                SearchScreenRoot(
                    onNavigateToProduct = { productId ->
                        dashboardNavController.navigate(ProductDetailRoute(productId))
                    },
                    onNavigateBack = { dashboardNavController.popBackStack() }
                )
            }

            // --- TAB 3: CART ---
            composable<CartRoute> {
                CartScreenRoot(
                    onNavigateToCheckout = {
                        dashboardNavController.navigate(CheckoutRoute)
                    },
                    onNavigateBack = {
                        // If accessed via tab, maybe do nothing or go home.
                        // If accessed via stack, pop back.
                        if (dashboardNavController.previousBackStackEntry != null) {
                            dashboardNavController.popBackStack()
                        } else {
                            dashboardNavController.navigate(HomeRoute)
                        }
                    }
                )
            }

            // --- TAB 4: PROFILE ---
            composable<ProfileRoute> {
                ProfileScreenRoot(
                    onNavigateToOrders = { dashboardNavController.navigate(OrdersRoute) },
                    onNavigateToWishlist = { dashboardNavController.navigate(WishlistRoute) },
                    onNavigateToAuth = onLogout)
            }

            // --- DETAILS (Full Screen / No Bottom Bar) ---
            composable<ProductDetailRoute> { backStackEntry ->
                val route = backStackEntry.toRoute<ProductDetailRoute>()
                ProductDetailScreenRoot(
                    productId = route.id,
                    onNavigateBack = { dashboardNavController.popBackStack() },
                    onNavigateToCart = {
                        // Switch to Cart Tab
                        dashboardNavController.navigate(CartRoute) {
                            popUpTo(dashboardNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }

            // --- CHECKOUT (Full Screen / No Bottom Bar) ---
            composable<CheckoutRoute> {
                CheckoutScreenRoot(
                    onNavigateBack = { dashboardNavController.popBackStack() },
                    onOrderSuccess = {
                        // Navigate to Orders Tab or Home (Mocking Home for now)
                        dashboardNavController.navigate(HomeRoute) {
                            popUpTo(HomeRoute) { inclusive = true }
                        }
                    }
                )
            }

            composable<OrdersRoute> {
                OrdersScreenRoot(
                    onNavigateBack = { dashboardNavController.popBackStack() } // Or rootNavController depending on where you put it
                )
            }

            composable<WishlistRoute> {
                WishlistScreenRoot(
                    onNavigateBack = { dashboardNavController.popBackStack() },
                    onNavigateToProduct = { productId ->
                        dashboardNavController.navigate(ProductDetailRoute(productId))
                    }
                )
            }
        }
    }
}
