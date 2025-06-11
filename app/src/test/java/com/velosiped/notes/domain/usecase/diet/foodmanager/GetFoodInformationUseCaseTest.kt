package com.velosiped.notes.domain.usecase.diet.foodmanager

import com.google.common.truth.Truth.assertThat
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.presentation.screens.diet.foodManagerScreen.FoodInput
import org.junit.Before
import org.junit.Test

class GetFoodInformationUseCaseTest {
    private lateinit var getFoodInformation: GetFoodInformationUseCase

    @Before
    fun setup() {
        getFoodInformation = GetFoodInformationUseCase()
    }

    @Test
    fun `Select existing food, get it`() {
        val food = Food(id = 1, name = "merry", protein = 12.5, fat = 55.6, carbs = 36.0, imageUrl = "christmas")
        val foodInput = FoodInput(name = "merry", protein = "12.5", fat = "55.6", carbs = "36.0", imageUri = "christmas")
        val pickedFood = getFoodInformation(food)
        assertThat(pickedFood).isEqualTo(foodInput)
    }

    @Test
    fun `Select non existing food, get default food`() {
        val pickedFood = getFoodInformation(null)
        assertThat(pickedFood).isEqualTo(FoodInput())
    }
}