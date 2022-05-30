package com.kebunby.kebunby.ui.feature.camera

import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.kebunby.kebunby.R
import com.kebunby.kebunby.ui.common.component.CustomToolbar
import com.kebunby.kebunby.ui.feature.camera.component.CameraView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun CameraScreen(
    onBackButtonClicked: () -> Unit,
    onImageCaptured: (File) -> Unit,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope
) {
    val context = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CustomToolbar(
                title = stringResource(id = R.string.take_plant_photo),
                hasBackButton = true,
                onBackButtonClicked = onBackButtonClicked
            )
        }
    ) {
        CameraView(
            context = context,
            onImageCaptured = { file ->
                onImageCaptured(file)
            },
            onError = { exception ->
                coroutineScope.launch {
                    exception.localizedMessage?.let { message ->
                        scaffoldState.snackbarHostState.showSnackbar(
                            message
                        )
                    }
                }
            }
        )
    }
}