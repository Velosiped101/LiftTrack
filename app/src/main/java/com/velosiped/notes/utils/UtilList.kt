package com.velosiped.notes.utils

import com.velosiped.notes.R
import java.util.Calendar
import kotlin.math.abs

const val EMPTY_STRING = ""
const val SPACE = " "
const val DOUBLE_PATTERN = "^\\d{0,2}(\\.\\d{0,2})?$"
const val INT_PATTERN = "^\\d*$"
const val GENERATED_ID_INITIAL = 2000000000

fun ClosedFloatingPointRange<Float>.toFloatList(step: Float): List<Float> {
    if (step <= 0f) throw IllegalArgumentException("Step value must be positive")
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

enum class DayProgress {
    Rest,
    Training,
    TrainingFinished
}

enum class WeightIncrement(val weight: Float) {
    Large(5f),
    Big(2.5f),
    Medium(1.25f),
    Small(1f),
    VerySmall(.5f)
}

enum class SearchMode {
    Local,
    Remote
}

enum class DayOfWeek {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday
}

enum class ExerciseType {
    Chest,
    Back,
    Shoulders,
    Arms,
    Core,
    Legs
}

data object Date {
    private val calendar: Calendar = Calendar.getInstance()
    private val daysMap = mapOf(
        1 to DayOfWeek.Sunday,
        2 to DayOfWeek.Monday,
        3 to DayOfWeek.Tuesday,
        4 to DayOfWeek.Wednesday,
        5 to DayOfWeek.Thursday,
        6 to DayOfWeek.Friday,
        7 to DayOfWeek.Saturday
    )

    private val dayIndex: Int = calendar.get(Calendar.DAY_OF_WEEK)
    val dayOfWeek: String = daysMap[dayIndex].toString()

    val day: Int = calendar.get(Calendar.DAY_OF_MONTH)
    val month: Int = calendar.get(Calendar.MONTH) + 1
    val year: Int = calendar.get(Calendar.YEAR)

    fun getMonthNameId(month: Int): Int? {
        val monthMap = mapOf(
            1 to R.string.january,
            2 to R.string.february,
            3 to R.string.march,
            4 to R.string.april,
            5 to R.string.may,
            6 to R.string.june,
            7 to R.string.july,
            8 to R.string.august,
            9 to R.string.september,
            10 to R.string.october,
            11 to R.string.november,
            12 to R.string.december
        )
        return monthMap[month]
    }
}

enum class Nutrient {
    Protein,
    Fat,
    Carbs
}