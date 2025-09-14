package com.velosiped.notes.utils

object Constants {
    const val EMPTY_STRING = ""
    const val SPACE = " "
    const val DOUBLE_PATTERN = "^\\d{0,2}(\\.\\d{0,2})?$"
    const val GENERATED_ID_INITIAL = 2000000000
    const val CAL_PER_GRAM_PROTEIN_CARB = 4
    const val CAL_PER_GRAM_FAT = 9
}

val Float.Companion.ZERO get() = 0f
val Float.Companion.DELTA_INCREMENT get() = .001f
val Float.Companion.QUARTER get() = .25f
val Float.Companion.ONE_THIRD get() = .33f
val Float.Companion.HALF get() = .5f
val Float.Companion.TWO_THIRDS get() = .66f
val Float.Companion.ONE get() = 1f
val Float.Companion.THOUSAND get() = 1000f

val Float.Companion.FULL_CIRCLE_ANGLE get() = 360f
val Float.Companion.TWO_QUARTERS_CIRCLE_ANGLE get() = 270f
val Float.Companion.HALF_CIRCLE_ANGLE get() = 180f
val Float.Companion.ONE_QUARTER_CIRCLE_ANGLE get() = 90f

val Float.Companion.TEN_PERCENT get() = .1f
val Float.Companion.TWENTY_PERCENT get() = .2f
val Float.Companion.NINETY_PERCENT get() = .9f



val Int.Companion.ZERO get() = 0
val Int.Companion.ONE get() = 1
val Int.Companion.TWO get() = 2
val Int.Companion.HUNDRED get() = 100

val String.Companion.EMPTY get() = ""
val String.Companion.SPACE get() = " "