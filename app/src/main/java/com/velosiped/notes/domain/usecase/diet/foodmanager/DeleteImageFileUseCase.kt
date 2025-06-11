package com.velosiped.notes.domain.usecase.diet.foodmanager

import android.content.Context
import android.util.Log
import androidx.core.net.toUri

class DeleteImageFileUseCase {
    operator fun invoke(imageUri: String?, context: Context) {
        val uri = imageUri?.toUri()
        uri?.let {
            try {
                context.contentResolver.delete(it, null, null)
            } catch (e: Exception) {
                Log.e("image delete error", e.message.toString())
            }
        }
    }
}