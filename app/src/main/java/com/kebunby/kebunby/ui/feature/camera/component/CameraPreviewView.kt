package com.kebunby.kebunby.ui.feature.camera.component

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.kebunby.kebunby.ui.feature.camera.CameraEvent
import com.kebunby.kebunby.util.getCameraProvider
import compose.icons.EvaIcons
import compose.icons.evaicons.Outline
import compose.icons.evaicons.outline.Camera
import compose.icons.evaicons.outline.Flip2
import compose.icons.evaicons.outline.Image

@Composable
fun CameraPreviewView(
    context: Context,
    imageCapture: ImageCapture,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    onCameraEvent: (CameraEvent) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(9f),
            factory = { previewView }
        )
        Spacer(modifier = Modifier.height(20.dp))
        CameraMenu(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 20.dp),
            onCameraEvent = onCameraEvent
        )
    }
}

@Composable
fun CameraMenu(
    modifier: Modifier = Modifier,
    onCameraEvent: (CameraEvent) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier.size(60.dp),
            onClick = { onCameraEvent(CameraEvent.SwitchLens) }
        ) {
            Icon(
                imageVector = EvaIcons.Outline.Flip2,
                tint = MaterialTheme.colors.onBackground,
                contentDescription = "Switch camera lens icon"
            )
        }
        Button(
            modifier = Modifier.size(60.dp),
            shape = MaterialTheme.shapes.large,
            onClick = { onCameraEvent(CameraEvent.Capture) }
        ) {
            Icon(
                imageVector = EvaIcons.Outline.Camera,
                contentDescription = "Camera icon"
            )
        }
        IconButton(
            modifier = Modifier.size(60.dp),
            onClick = { onCameraEvent(CameraEvent.ViewGallery) }
        ) {
            Icon(
                imageVector = EvaIcons.Outline.Image,
                tint = MaterialTheme.colors.onBackground,
                contentDescription = "View gallery icon"
            )
        }
    }
}