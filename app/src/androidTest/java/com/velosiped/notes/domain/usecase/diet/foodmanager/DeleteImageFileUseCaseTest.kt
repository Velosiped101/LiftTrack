package com.velosiped.notes.domain.usecase.diet.foodmanager

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.test.core.app.ApplicationProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class DeleteImageFileUseCaseTest {
    private lateinit var deleteImageFile: DeleteImageFileUseCase
    private lateinit var uri: Uri
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        deleteImageFile = DeleteImageFileUseCase()
    }

    @Test
    fun deleteFileWithValidUri_functionInvokedWithValidUri() {
        val file = File(context.getExternalFilesDir("NotesImages"), "file.jpg")
        file.writeText("text")
        assert(file.exists())
        uri = FileProvider.getUriForFile(context, "${context.packageName}.fileProvider", file)
        deleteImageFile(uri.toString(), context)
        assert(!file.exists())
    }

    @Test
    fun deleteFileWithNullUri_functionDoesNothing() {
        deleteImageFile(null, context)
    }
}