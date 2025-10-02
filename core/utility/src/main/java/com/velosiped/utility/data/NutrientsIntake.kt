package com.velosiped.utility.data

import com.velosiped.utility.constants.Constants
import com.velosiped.utility.extensions.ZERO

data class NutrientsIntake(
    val protein: Float = Float.ZERO,
    val fat: Float = Float.ZERO,
    val carbs: Float = Float.ZERO
) {
    val calories: Int
        get() = ((protein + carbs) * Constants.CAL_PER_GRAM_PROTEIN_CARB + fat * Constants.CAL_PER_GRAM_FAT).toInt()
}
