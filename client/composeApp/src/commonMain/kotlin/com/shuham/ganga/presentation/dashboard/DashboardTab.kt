package com.shuham.ganga.presentation.dashboard

import com.shuham.ganga.presentation.navigation.CartRoute
import com.shuham.ganga.presentation.navigation.HomeRoute
import com.shuham.ganga.presentation.navigation.ProfileRoute
import com.shuham.ganga.presentation.navigation.SearchRoute
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
import kotlin.reflect.KClass

enum class DashboardTab(
    val route: KClass<*>,
    val label: String,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource
) {
    Home(HomeRoute::class,"Home", Res.drawable.ic_home_fill, Res.drawable.ic_home),
    Search(SearchRoute::class,"Search", Res.drawable.ic_search_fill, Res.drawable.ic_search),
    Cart(CartRoute::class,"Cart", Res.drawable.ic_shopping_cart_fill, Res.drawable.ic_shopping_cart),
    Profile(ProfileRoute::class,"Profile", Res.drawable.ic_person_fill, Res.drawable.ic_person)
}