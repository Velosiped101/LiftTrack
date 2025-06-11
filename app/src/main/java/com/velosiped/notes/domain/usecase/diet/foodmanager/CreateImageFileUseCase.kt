package com.velosiped.notes.domain.usecase.diet.foodmanager

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

class CreateImageFileUseCase {
    operator fun invoke(context: Context): Uri? {
        return try {
            val imageDir =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NotesImages")
            imageDir.mkdir()
            val image = File(imageDir, "img_${System.currentTimeMillis()}.jpg")
            FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", image)
        } catch (e: Exception) {
            null
        }
    }
}