package com.shuham.ganga.presentation.dashboard

import ganga.composeapp.generated.resources.Res
import ganga.composeapp.generated.resources.ic_home
import ganga.composeapp.generated.resources.ic_home_fill
import ganga.composeapp.generated.resources.ic_person
import ganga.composeapp.generated.resources.ic_person_fill
import ganga.composeapp.generated.resources.ic_search
import ganga.composeapp.generated.resources.ic_search_fill
import ganga.composeapp.generated.resources.ic_shopping_cart_fill
import ganga.composeapp.generated.resources.ic_shopping_cart
import org.jetbrains.compose.resources.DrawableResource

enum class DashboardTab(
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource
) {
    Home("Home", Res.drawable.ic_home_fill, Res.drawable.ic_home),
    Search("Search", Res.drawable.ic_search_fill, Res.drawable.ic_search),
    Cart("Cart", Res.drawable.ic_shopping_cart_fill, Res.drawable.ic_shopping_cart),
    Profile("Profile", Res.drawable.ic_person_fill, Res.drawable.ic_person)
}