package com.velosiped.notes.domain.usecase.diet.addmeal

import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.domain.repository.DietRepository
import com.velosiped.notes.utils.Constants
import java.util.Calendar
import javax.inject.Inject

class ConfirmMealAdditionUseCase @Inject constructor(
    private val dietRepository: DietRepository
) {
    suspend operator fun invoke(foodList: Map<Food, Int>) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY).let {
            if (it < 10) "0$it" else it.toString()
        }
        val minute = calendar.get(Calendar.MINUTE).let {
            if (it < 10) "0$it" else it.toString()
        }
        val time = "$hour : $minute"
        foodList.entries.forEach {
            val totalCals = ((it.key.carbs + it.key.protein) * Constants.CAL_PER_GRAM_PROTEIN_CARB
                    + it.key.fat * Constants.CAL_PER_GRAM_FAT) * it.value/100
            val meal = MealHistory(
                time = time,
                name = it.key.name,
                protein = it.key.protein.toFloat(),
                fat = it.key.fat.toFloat(),
                carbs = it.key.carbs.toFloat(),
                mass = it.value
            )
            dietRepository.insertToHistory(meal)
        }
    }
}