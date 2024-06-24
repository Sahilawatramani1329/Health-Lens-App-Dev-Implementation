package com.example.healthlens3

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.IOException

object FileUtil {
    fun from(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri) ?: throw IOException("Unable to open input stream from URI")
        val file = File(context.cacheDir, "temp_image") // Consider using a unique filename
        file.outputStream().use {
            inputStream.copyTo(it)
        }
        return file
    }
}
