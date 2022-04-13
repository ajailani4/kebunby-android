package com.kebunby.kebunby.ui

sealed class Screen(val route: String) {
    object OnboardingScreen : Screen("onboarding_screen")
    object LoginScreen : Screen("login_screen")
    object HomeScreen : Screen("home_screen")
}
