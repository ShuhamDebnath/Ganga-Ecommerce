package com.shuham.ganga.presentation.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.shuham.ganga.presentation.dashboard.components.DashboardBottomBar
import com.shuham.ganga.presentation.dashboard.home.HomeScreenRoot

@Composable
fun DashboardScreenRoot(
    onNavigateToProduct: (String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(DashboardTab.Home) }

    Scaffold(
        bottomBar = {
            DashboardBottomBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedTab) {
                DashboardTab.Home -> HomeScreenRoot(onNavigateToProduct = onNavigateToProduct)
                DashboardTab.Search -> PlaceholderScreen("Search")
                DashboardTab.Cart -> PlaceholderScreen("Cart")
                DashboardTab.Profile -> PlaceholderScreen("Profile")
            }
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Coming Soon: $name Screen", color = Color.Gray)
    }
}