package com.velosiped.utility.camerahelper

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File

class CameraHelper(private val context: Context) {

    fun createFile(cachedFileUri: Uri): Uri? {
        val sourceStream = context.contentResolver.openInputStream(cachedFileUri)
        val imageDir =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), EXTERNAL_FILES_DIR_NAME)
        imageDir.mkdir()
        val targetFile = File(imageDir, "${System.currentTimeMillis()}.$FILE_FORMAT")

        try {
            sourceStream.use { source ->
                targetFile.outputStream().use { target ->
                    source?.copyTo(target)
                }
            }
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
        }
        return FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.$AUTHORITY",
            targetFile
        )
    }

    fun createTemporaryFile(): Uri? = try {
        val cacheDir = File(context.externalCacheDir, EXTERNAL_CACHE_DIR_NAME)
        cacheDir.mkdir()
        val image = File.createTempFile("${System.currentTimeMillis()}", ".$FILE_FORMAT", cacheDir)
        FileProvider.getUriForFile(
            context,
            "${context.applicationContext.packageName}.$AUTHORITY",
            image
        )
    } catch (e: Exception) {
        Log.e(LOG_TAG, e.message.toString())
        null
    }

    fun deleteFile(uri: Uri?) = uri?.let { context.contentResolver.delete(it, null, null) }

    companion object {
        const val EXTERNAL_FILES_DIR_NAME = "NotesImages"
        const val EXTERNAL_CACHE_DIR_NAME = "NotesImages"
        const val FILE_FORMAT = "jpg"
        const val AUTHORITY = "fileProvider"
        const val LOG_TAG = "Camera helper"
    }

}