package com.velosiped.notes.domain

class TextFieldValidator {
    fun validateMass(input: String): Boolean =
        input.matches(Regex(INT_REGEX)) &&
        input.length <= MASS_MAX_INPUT_LENGTH &&
        (input.toIntOrNull() != null || input.isBlank())

    companion object {
        const val MASS_MAX_INPUT_LENGTH = 3
        const val INT_REGEX = "^\\d*$"
    }
}