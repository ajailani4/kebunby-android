package com.kebunby.kebunby.ui

sealed class Screen(val route: String) {
    object LoginScreen : Screen("login_screen")
}
