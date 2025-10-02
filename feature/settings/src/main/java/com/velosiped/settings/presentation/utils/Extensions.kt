package com.velosiped.settings.presentation.utils

import kotlin.math.abs

fun ClosedFloatingPointRange<Float>.toFloatList(step: Float): List<Float> {
    val list = mutableListOf<Float>()
    var currentValue = start
    while (currentValue <= endInclusive) {
        list.add(currentValue)
        currentValue += step
    }
    return list
}

fun <T: Number> List<T>.getClosestIndex(value: T): Int =
    minByOrNull { abs(it.toFloat() - value.toFloat()) }?.let { indexOf(it) } ?: 0