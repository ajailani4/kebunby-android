package com.kebunby.kebunby.util

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.io.File
import java.io.InputStream
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { cameraProvider ->
        cameraProvider.addListener(
            {
                continuation.resume(cameraProvider.get())
            },
            ContextCompat.getMainExecutor(this)
        )
    }
}

fun ImageCapture.takePicture(
    context: Context,
    onImageCaptured: (File) -> Unit,
    onError: (ImageCaptureException) -> Unit
) {
    val photoFile = File(context.cacheDir, "${UUID.randomUUID()}.jpg")
    val outputFileOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    this.takePicture(
        outputFileOptions,
        Executors.newSingleThreadExecutor(),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = outputFileResults.savedUri ?: Uri.fromFile(photoFile)
                onImageCaptured(File(savedUri.path!!))
            }

            override fun onError(exception: ImageCaptureException) {
                onError(exception)
            }
        }
    )
}

// This is used if image is picked from gallery
fun Context.convertInputStreamToFile(inputStream: InputStream): File {
    val file = File(cacheDir, "${UUID.randomUUID()}.jpg")

    inputStream.use { input ->
        file.outputStream().use { output ->
            input.copyTo(output)
        }
    }

    return file
}

fun File.getFileSizeInMB() = length() / 1048576