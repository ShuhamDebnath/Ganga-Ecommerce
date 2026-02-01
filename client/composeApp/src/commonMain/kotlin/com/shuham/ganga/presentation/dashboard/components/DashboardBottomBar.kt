package com.shuham.ganga.presentation.dashboard.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shuham.ganga.presentation.dashboard.DashboardTab
import com.shuham.ganga.presentation.theme.GangaOrange
import com.shuham.ganga.presentation.theme.GangaOrangeLight
import org.jetbrains.compose.resources.painterResource

@Composable
fun DashboardBottomBar(
    selectedTab: DashboardTab,
    onTabSelected: (DashboardTab) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = GangaOrange,
        tonalElevation = 8.dp
    ) {
        DashboardTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Icon(
                        painter = if (isSelected) painterResource(tab.selectedIcon) else painterResource(tab.unselectedIcon),
                        contentDescription = tab.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = tab.label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                },
                // This enables the animation: Label hides when unselected, slides in when selected.
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = GangaOrange,
                    selectedTextColor = GangaOrange,
                    indicatorColor = GangaOrangeLight,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
            )
        }
    }
}

@Preview
@Composable
fun DashboardBottomBarPrev() {
    val selectedTab = DashboardTab.Home
    DashboardBottomBar(selectedTab) {
        println("Tab selected: $it")
    }
}

