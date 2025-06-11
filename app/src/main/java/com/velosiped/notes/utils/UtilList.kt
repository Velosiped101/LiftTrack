package com.velosiped.notes.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.velosiped.notes.R
import com.velosiped.notes.data.api.foodApi.Product
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodInput
import com.velosiped.notes.proto.ProtoProgramProgress
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Calendar
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

fun FoodInput.toFood(): Food {
    return Food(
        name = this.name,
        protein = this.protein.toDouble(),
        fat = this.fat.toDouble(),
        carbs = this.carbs.toDouble(),
        imageUrl = this.imageUri
    )
}

fun Food?.toFoodInput(): FoodInput = this?.let {
    FoodInput(
        name = this.name,
        protein = this.protein.toString(),
        fat = this.fat.toString(),
        carbs = this.carbs.toString(),
        imageUri = this.imageUrl
    )
} ?: FoodInput()

fun <T: Number> List<T>.getClosestIndex(value: T): Int =
    minByOrNull { abs(it.toFloat() - value.toFloat()) }?.let { indexOf(it) } ?: 0

fun List<Product>?.toFoodList(): List<Food> {
    return this?.filter { params ->
        listOf(
            params.productName,
            params.nutriments,
            params.nutriments?.proteins,
            params.nutriments?.fat,
            params.nutriments?.carbohydrates
        ).none { it == null }
    }?.map {
        Food(
            name = it.productName!!,
            protein = it.nutriments!!.proteins!!.toDouble().cut(),
            fat = it.nutriments.fat!!.toDouble().cut(),
            carbs = it.nutriments.carbohydrates!!.toDouble().cut(),
            imageUrl = it.imageUrl
        )
    } ?: listOf()
}

fun Double.cut(): Double {
    return BigDecimal(this).setScale(2, RoundingMode.HALF_UP).toDouble()
}

fun List<ProtoProgramProgress>.toProgramProgressList(): List<ProgramProgress> {
    return this.map {
        ProgramProgress(
            day = it.day,
            month = it.month,
            year = it.year,
            exercise = it.exercise,
            reps = it.reps,
            repsPlanned = it.repsPlanned,
            weight = it.weight
        )
    }
}

fun List<ProgramProgress>.toProgramTempProgressItemsList(): List<ProtoProgramProgress> {
    return this.map {
        ProtoProgramProgress
            .newBuilder()
            .setDay(it.day)
            .setMonth(it.month)
            .setYear(it.year)
            .setExercise(it.exercise)
            .setReps(it.reps)
            .setRepsPlanned(it.repsPlanned)
            .setWeight(it.weight)
            .build()
    }
}

fun interpolateColors(t: Float, colors: List<Color>): Color {
    val segment = when {
        t < .33f -> 0
        t < .66f -> 1
        else -> 2
    }
    val localT = when (segment) {
        0 -> t / .33f
        1 -> (t - .33f) / (.33f)
        else -> (t - .66f) / (.33f)
    }
    val colorStart = colors[segment]
    val colorEnd = colors[segment + 1]
    return lerp(colorStart, colorEnd, localT)
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