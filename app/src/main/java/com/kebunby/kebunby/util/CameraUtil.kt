package com.kebunby.kebunby.util

import android.content.Context
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.io.File
import java.io.InputStream
import java.util.*
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