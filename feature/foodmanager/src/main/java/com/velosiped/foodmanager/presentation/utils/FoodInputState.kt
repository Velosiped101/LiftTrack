package com.velosiped.foodmanager.presentation.utils

import android.net.Uri
import com.velosiped.ui.model.textfieldvalidator.TextFieldState

data class FoodInputState(
    val id: Long? = null,
    val imageUri: Uri? = null,
    val nameFieldState: TextFieldState = TextFieldState(),
    val proteinFieldState: TextFieldState = TextFieldState(),
    val fatFieldState: TextFieldState = TextFieldState(),
    val carbsFieldState: TextFieldState = TextFieldState()
) {
    val noEmptyFields: Boolean
        get() = listOf(
            nameFieldState.text,
            proteinFieldState.text,
            fatFieldState.text,
            carbsFieldState.text
        ).none { it.isEmpty() }
}