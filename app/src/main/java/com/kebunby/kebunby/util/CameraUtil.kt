package com.kebunby.kebunby.util

import android.content.Context
import java.io.File
import java.io.InputStream
import java.util.*

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