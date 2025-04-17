package com.example.notes.utils

import java.util.Calendar

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
    All,
    Chest,
    Back,
    Shoulders,
    Arms,
    Core,
    Legs
}

const val EMPTY_STRING = ""
const val DOUBLE_PATTERN = "^\\d*(\\.\\d{0,2})?$"

data object Date {
    private val calendar = Calendar.getInstance()
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
    val month: Int = calendar.get(Calendar.MONTH)
    val year: Int = calendar.get(Calendar.YEAR)
}

enum class Nutrient {
    Protein,
    Fat,
    Carbs
}