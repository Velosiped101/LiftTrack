package com.velosiped.ui.model.textfieldvalidator

class TextFieldValidator {
    fun validateMass(input: String): Boolean {
        val trimmedInput = input.trim()
        if (trimmedInput.isEmpty()) return true
        if (!trimmedInput.matches(Regex(INT_REGEX))) return false
        val value = trimmedInput.toIntOrNull() ?: return false
        return value <= MASS_VALUE_MAX
    }

    fun validateNutrient(input: String): Boolean {
        val trimmedInput = input.trim()
        if (trimmedInput.isEmpty()) return true
        if (!trimmedInput.matches(Regex(FLOAT_REGEX))) return false
        val value = trimmedInput.toFloatOrNull() ?: return false
        return value in NUTRIENT_VALUE_MIN..NUTRIENT_VALUE_MAX
    }

    companion object {
        const val MASS_VALUE_MAX = 1000
        const val INT_REGEX = "^\\d*$"
        const val FLOAT_REGEX = """^-?\d*([.,]?\d{0,2})?$"""
        const val NUTRIENT_VALUE_MIN = 0f
        const val NUTRIENT_VALUE_MAX = 100f
    }
}