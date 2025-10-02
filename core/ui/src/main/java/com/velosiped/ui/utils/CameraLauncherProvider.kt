package com.velosiped.ui.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.velosiped.ui.R

@Composable fun getTakePhotoLauncher(
    uri: Uri?,
    context: Context,
    onSuccess: () -> Unit
): () -> Unit {
    val noPermissionMessage = stringResource(R.string.app_needs_camera_permission)

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            onSuccess()
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            uri?.let { cameraLauncher.launch(it) }
        } else {
            Toast.makeText(
                context,
                noPermissionMessage,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    return {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            uri?.let { cameraLauncher.launch(it) }
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
}