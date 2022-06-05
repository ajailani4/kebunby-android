package com.kebunby.app.ui

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kebunby.app.ui.theme.Grey
import com.kebunby.app.ui.theme.poppinsFamily

@Composable
fun BottomBar(navController: NavController) {
    val menus = listOf(
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
        val currentRoute = navBarStackEntry?.destination?.route

        menus.map { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = screen.icon!!,
                        contentDescription = stringResource(screen.title!!)
                    )
                },
                label = {
                    Text(
                        text = stringResource(screen.title!!),
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