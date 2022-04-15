package com.kebunby.kebunby.ui

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Home
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.Search
import compose.icons.evaicons.outline.ShoppingCart

sealed class Screen(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object OnboardingScreen : Screen("onboarding_screen")

    object LoginScreen : Screen("login_screen")

    object RegisterScreen : Screen("register_screen")

    // Bottom nav menu
    object HomeScreen : Screen(
        route = "home_screen",
        title = "Home",
        icon = EvaIcons.Outline.Home
    )

    object ExploreScreen : Screen(
        route = "explore_screen",
        title = "Explore",
        icon = EvaIcons.Outline.Search
    )

    object ShopScreen : Screen(
        route = "shop_screen",
        title = "Shop",
        icon = EvaIcons.Outline.ShoppingCart
    )

    object ProfileScreen : Screen(
        route = "profile_screen",
        title = "Profile",
        icon = EvaIcons.Outline.Person
    )
}
