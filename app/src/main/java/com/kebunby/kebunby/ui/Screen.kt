package com.kebunby.kebunby.ui

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.kebunby.kebunby.R
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Compass
import compose.icons.evaicons.outline.Home
import compose.icons.evaicons.outline.Person
import compose.icons.evaicons.outline.ShoppingCart

sealed class Screen(
    val route: String,
    @StringRes val title: Int? = null,
    val icon: ImageVector? = null
) {
    object OnboardingScreen : Screen("onboarding_screen")

    object LoginScreen : Screen("login_screen")

    object RegisterScreen : Screen("register_screen")

    object PlantListScreen : Screen("plant_list_screen")

    object PlantDetailScreen : Screen("plant_detail_screen")

    object UploadEditPlantScreen : Screen("upload_edit_plant_screen")

    // Bottom nav menu
    object HomeScreen : Screen(
        route = "home_screen",
        title = R.string.home,
        icon = EvaIcons.Outline.Home
    )

    object ExploreScreen : Screen(
        route = "explore_screen",
        title = R.string.explore,
        icon = EvaIcons.Outline.Compass
    )

    object ShopScreen : Screen(
        route = "shop_screen",
        title = R.string.shop,
        icon = EvaIcons.Outline.ShoppingCart
    )

    object ProfileScreen : Screen(
        route = "profile_screen",
        title = R.string.profile,
        icon = EvaIcons.Outline.Person
    )
}
