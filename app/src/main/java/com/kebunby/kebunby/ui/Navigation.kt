package com.kebunby.kebunby.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kebunby.kebunby.ui.common.SharedViewModel
import com.kebunby.kebunby.ui.feature.explore.ExploreScreen
import com.kebunby.kebunby.ui.feature.home.HomeScreen
import com.kebunby.kebunby.ui.feature.login.LoginScreen
import com.kebunby.kebunby.ui.feature.onboarding.OnboardingScreen
import com.kebunby.kebunby.ui.feature.plant_detail.PlantDetailScreen
import com.kebunby.kebunby.ui.feature.plant_list.PlantListScreen
import com.kebunby.kebunby.ui.feature.profile.ProfileScreen
import com.kebunby.kebunby.ui.feature.register.RegisterScreen
import com.kebunby.kebunby.ui.feature.shop.ShopScreen
import com.kebunby.kebunby.ui.feature.upload_edit_plant.UploadEditPlantScreen

@Composable
fun Navigation(
    navController: NavHostController,
    startDestination: String,
    sharedViewModel: SharedViewModel
) {
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
                "?isTrending={isTrending}&forBeginner={forBeginner}&categoryId={categoryId}&name={name}",
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
                navArgument("name") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            PlantListScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Screen.PlantDetailScreen.route + "?plantId={plantId}",
            arguments = listOf(
                navArgument("plantId") {
                    type = NavType.IntType
                }
            )
        ) {
            PlantDetailScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(
            route = Screen.UploadEditPlantScreen.route + "?plantId={plantId}",
            arguments = listOf(
                navArgument("plantId") {
                    type = NavType.IntType
                    defaultValue = 0
                }
            )
        ) {
            UploadEditPlantScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        // Bottom nav menu
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }

        composable(route = Screen.ExploreScreen.route) {
            ExploreScreen(navController)
        }

        composable(route = Screen.ShopScreen.route) {
            ShopScreen()
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(
                navController = navController,
                sharedViewModel = sharedViewModel
            )
        }
    }
}