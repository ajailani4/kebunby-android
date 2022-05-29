package com.kebunby.kebunby.ui.feature.upload_plant

import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.kebunby.kebunby.ui.feature.camera.CameraScreen

@Composable
fun UploadPlantScreen(navController: NavController) {
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState
    ) {
        CameraScreen(
            onBackButtonClicked = { navController.navigateUp() },
            onCameraScreenVisChanged = {},
            onImageCaptured = {},
            scaffoldState = scaffoldState,
            coroutineScope = coroutineScope
        )
    }
}