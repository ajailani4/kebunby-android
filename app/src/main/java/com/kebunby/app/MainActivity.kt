package com.kebunby.app

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.kebunby.app.ui.BottomBar
import com.kebunby.app.ui.Navigation
import com.kebunby.app.ui.Screen
import com.kebunby.app.ui.common.SharedViewModel
import com.kebunby.app.ui.feature.splash.SplashViewModel
import com.kebunby.app.ui.theme.KebunbyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels {
        SavedStateViewModelFactory(application, this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        lifecycleScope.launch {
            splashViewModel.getUserCredential().collect { userCredential ->
                val startDestination =
                    if (userCredential.username != "" && userCredential.accessToken != "") {
                        Screen.HomeScreen.route
                    } else {
                        Screen.OnboardingScreen.route
                    }

                setContent {
                    App {
                        Content(
                            startDestination = startDestination,
                            sharedViewModel = sharedViewModel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun App(content: @Composable () -> Unit) {
    KebunbyTheme {
        Surface(color = MaterialTheme.colors.background) {
            content()
        }
    }
}

@Composable
fun Content(
    startDestination: String,
    sharedViewModel: SharedViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val hasBottomMenuRoutes = listOf(
        Screen.HomeScreen.route,
        Screen.ExploreScreen.route,
        Screen.ShopScreen.route,
        Screen.ProfileScreen.route
    )

    Scaffold(
        bottomBar = {
            if (hasBottomMenuRoutes.contains(currentRoute)) {
                BottomBar(navController)

                (LocalContext.current as Activity).window
                    .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())) {
            Navigation(
                navController = navController,
                startDestination = startDestination,
                sharedViewModel = sharedViewModel
            )
        }
    }
}