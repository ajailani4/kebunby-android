package com.kebunby.kebunby

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.kebunby.kebunby.ui.Navigation
import com.kebunby.kebunby.ui.Screen
import com.kebunby.kebunby.ui.theme.KebunbyTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val startDestination = Screen.LoginScreen.route

        setContent {
            App {
                Content(startDestination)
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