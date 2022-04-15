package com.kebunby.kebunby.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kebunby.kebunby.ui.feature.home.HomeScreen
import com.kebunby.kebunby.ui.feature.login.LoginScreen
import com.kebunby.kebunby.ui.feature.onboarding.OnboardingScreen
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

        composable(route = Screen.HomeScreen.route) {
            HomeScreen()
        }
    }
}