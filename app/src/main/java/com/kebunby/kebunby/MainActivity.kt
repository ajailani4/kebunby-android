package com.kebunby.kebunby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.kebunby.kebunby.ui.Navigation
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.feature.splash.SplashViewModel
import com.kebunby.kebunby.ui.theme.KebunbyTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

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
                        Content(startDestination)
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
fun Content(startDestination: String) {
    val navController = rememberNavController()

    Scaffold {
        Navigation(navController = navController, startDestination = startDestination)
    }
}