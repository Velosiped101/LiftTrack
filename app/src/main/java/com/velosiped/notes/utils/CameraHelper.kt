package com.velosiped.notes.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import kotlin.io.path.Path
import kotlin.io.path.copyTo

class CameraHelper(private val context: Context) {

    fun createFile(cachedFileUri: Uri): Uri? {
        val sourceStream = context.contentResolver.openInputStream(cachedFileUri)
        val imageDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NotesImages")
        imageDir.mkdir()
        val targetFile = File(imageDir, "img_${System.currentTimeMillis()}.jpg")

        try {
            sourceStream.use { source ->
                targetFile.outputStream().use { target ->
                    source?.copyTo(target)
                }
            }
            Log.e("tag", "Transfer complete")
        } catch (e: Exception) {
            Log.e("tag", e.message.toString())
        }
        return FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.fileProvider",
            targetFile
        )
    }

    fun createTemporaryFile(): Uri? = try {
        val cacheDir = File(context.externalCacheDir, "NotesImages")
        cacheDir.mkdir()
        val image = File.createTempFile("img_${System.currentTimeMillis()}", ".jpg", cacheDir)
        FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.fileProvider",
            image
        ).apply {
            Log.e("tag", "Temp file uri: $this")
        }
    } catch (e: Exception) {
        Log.e("tag", e.message.toString())
        null
    }

    fun deleteFile(uri: Uri?) = uri?.let { context.contentResolver.delete(it, null, null) }

}