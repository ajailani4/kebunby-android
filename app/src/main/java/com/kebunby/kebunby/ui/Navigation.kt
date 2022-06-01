package com.kebunby.kebunby.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kebunby.kebunby.ui.feature.explore.ExploreScreen
import com.kebunby.kebunby.ui.feature.home.HomeScreen
import com.kebunby.kebunby.ui.feature.login.LoginScreen
import com.kebunby.kebunby.ui.feature.onboarding.OnboardingScreen
import com.kebunby.kebunby.ui.feature.plant_detail.PlantDetailScreen
import com.kebunby.kebunby.ui.feature.plant_list.PlantListScreen
import com.kebunby.kebunby.ui.feature.profile.ProfileScreen
import com.kebunby.kebunby.ui.feature.register.RegisterScreen
import com.kebunby.kebunby.ui.feature.upload_plant.UploadPlantScreen

@Composable
fun Navigation(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(route = Screen.OnboardingScreen.route) {
            OnboardingScreen(navController)
        }

        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController)
        }

        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController)
        }

        composable(
            route = Screen.PlantListScreen.route +
                "?isTrending={isTrending}&forBeginner={forBeginner}&categoryId={categoryId}&category={category}",
            arguments = listOf(
                navArgument("isTrending") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("forBeginner") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("categoryId") {
                    type = NavType.IntType
                    defaultValue = 0
                },
                navArgument("category") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            PlantListScreen(navController)
        }

        composable(
            route = Screen.PlantDetailScreen.route + "?plantId={plantId}",
            arguments = listOf(
                navArgument("plantId") {
                    type = NavType.IntType
                }
            )
        ) {
            PlantDetailScreen(navController)
        }

        composable(route = Screen.UploadPlantScreen.route) {
            UploadPlantScreen(navController)
        }

        // Bottom nav menu
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(navController)
        }

        composable(route = Screen.ShopScreen.route) {
            // ShopScreen()
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController)
        }
    }
}