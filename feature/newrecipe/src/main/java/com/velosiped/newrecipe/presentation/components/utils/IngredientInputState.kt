package com.velosiped.newrecipe.presentation.components.utils

import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import java.util.UUID

data class IngredientInputState(
    val id: Long? = null,
    val tempId: UUID = UUID.randomUUID(),
    val nameFieldState: TextFieldState = TextFieldState(),
    val proteinFieldState: TextFieldState = TextFieldState(),
    val fatFieldState: TextFieldState = TextFieldState(),
    val carbsFieldState: TextFieldState = TextFieldState(),
    val massFieldState: TextFieldState = TextFieldState(),
    val readOnly: Boolean = false
) {
    val noEmptyFields: Boolean
        get() = listOf(
            nameFieldState.text,
            proteinFieldState.text,
            fatFieldState.text,
            carbsFieldState.text,
            massFieldState.text
        ).none { it.isBlank() }
}