package com.velosiped.utility.extensions

import java.math.RoundingMode

val Float.Companion.ZERO get() = 0f
val Float.Companion.QUARTER get() = .25f
val Float.Companion.ONE_THIRD get() = .33f
val Float.Companion.HALF get() = .5f
val Float.Companion.TWO_THIRDS get() = .66f
val Float.Companion.ONE get() = 1f
val Float.Companion.THOUSAND get() = 1000f

val Float.Companion.FULL_CIRCLE_ANGLE get() = 360f
val Float.Companion.TWO_QUARTERS_CIRCLE_ANGLE get() = 270f
val Float.Companion.HALF_CIRCLE_ANGLE get() = 180f

val Float.Companion.TEN_PERCENT get() = .1f
val Float.Companion.TWENTY_PERCENT get() = .2f
val Float.Companion.NINETY_PERCENT get() = .9f

fun Float.cut(): Float {
    return this.toBigDecimal().setScale(Int.ONE, RoundingMode.HALF_UP).toFloat()
}