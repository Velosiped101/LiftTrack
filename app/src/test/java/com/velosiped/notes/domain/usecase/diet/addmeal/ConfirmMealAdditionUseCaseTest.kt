package com.velosiped.notes.domain.usecase.diet.addmeal

import com.google.common.truth.Truth.assertThat
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.repository.diet.FakeDietRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ConfirmMealAdditionUseCaseTest {
    private lateinit var confirmMealAddition: ConfirmMealAdditionUseCase
    private lateinit var repository: FakeDietRepository

    @Before
    fun setup() {
        repository = FakeDietRepository()
        confirmMealAddition = ConfirmMealAdditionUseCase(repository)
    }

    @Test
    fun `Insert 2 meal history rows, meal history size equals 2`(): Unit = runBlocking {
        val foodList = mapOf(
            Food(id = 20, name = "item 1", protein = 2.0, fat = 12.5, carbs = 22.0, imageUrl = null) to 200,
            Food(id = 25, name = "item 2", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null) to 300
        )
        confirmMealAddition(foodList)
        val mealHistory = repository.getMealHistory().first()
        assertThat(mealHistory.size).isEqualTo(2)
    }

    @Test
    fun `Insert 3 meal history rows, meal history size equals 3`(): Unit = runBlocking {
        val foodList = mapOf(
            Food(id = 20, name = "item 1", protein = 2.0, fat = 12.5, carbs = 22.0, imageUrl = null) to 200,
            Food(id = 25, name = "item 2", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null) to 300,
            Food(id = 30, name = "item 3", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null) to 500
        )
        confirmMealAddition(foodList)
        val mealHistory = repository.getMealHistory().first()
        assertThat(mealHistory.size).isEqualTo(3)
    }

    @Test
    fun `Insert nothing, meal history size equals 0`(): Unit = runBlocking {
        val foodList = emptyMap<Food, Int>()
        confirmMealAddition(foodList)
        val mealHistory = repository.getMealHistory().first()
        assertThat(mealHistory.size).isEqualTo(0)
    }
}