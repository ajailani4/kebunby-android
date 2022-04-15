package com.kebunby.kebunby.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kebunby.kebunby.ui.theme.Grey
import com.kebunby.kebunby.ui.theme.poppinsFamily

@Composable
fun BottomBar(navController: NavController) {
    val items = listOf(
        Screen.HomeScreen,
        Screen.ExploreScreen,
        Screen.ShopScreen,
        Screen.ProfileScreen
    )

    BottomNavigation(
        elevation = 20.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        val navBarStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute= navBarStackEntry?.destination?.route

        items.map { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = screen.title
                    )
                },
                label = {
                    Text(
                        text = screen.title!!,
                        fontFamily = poppinsFamily
                    )
                },
                selected = currentRoute == screen.route,
                selectedContentColor = MaterialTheme.colors.primary,
                unselectedContentColor = Grey,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.HomeScreen.route) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}