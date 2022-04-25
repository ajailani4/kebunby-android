package com.kebunby.kebunby.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kebunby.kebunby.ui.feature.home.HomeScreen
import com.kebunby.kebunby.ui.feature.login.LoginScreen
import com.kebunby.kebunby.ui.feature.onboarding.OnboardingScreen
import com.kebunby.kebunby.ui.feature.plant_list.PlantListScreen
import com.kebunby.kebunby.ui.feature.register.RegisterScreen

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

        composable(route = Screen.PlantListScreen.route) {
            PlantListScreen(navController)
        }

        // Bottom nav menu
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController)
        }

        composable(route = Screen.ExploreScreen.route) {
            // ExploreScreen()
        }

        composable(route = Screen.ShopScreen.route) {
            // ShopScreen()
        }

        composable(route = Screen.ProfileScreen.route) {
            // ProfileScreen()
        }
    }
}