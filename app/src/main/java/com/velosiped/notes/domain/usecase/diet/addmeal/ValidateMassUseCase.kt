package com.velosiped.notes.domain.usecase.diet.addmeal

import com.velosiped.notes.utils.Constants

class ValidateMassUseCase {
    operator fun invoke(input: String): Int? {
        return if ((input.matches(Regex(Constants.INT_PATTERN)))
            && input.length <= Constants.MAX_MASS_INPUT_LENGTH) input.toIntOrNull()
        else null
    }
}