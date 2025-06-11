package com.velosiped.notes.domain.usecase.diet.addmeal

import com.google.common.truth.Truth.assertThat
import com.velosiped.notes.data.database.food.Food
import org.junit.Before
import org.junit.Test

class ManagePickedFoodListUseCaseTest {
    private lateinit var useCase: ManagePickedFoodListUseCase
    private lateinit var initialMap: Map<Food, Int>

    @Before
    fun setup() {
        useCase = ManagePickedFoodListUseCase()
        initialMap = mapOf(
            Food(id = 20, name = "item 1", protein = 2.0, fat = 12.5, carbs = 22.0, imageUrl = null) to 200,
            Food(id = 25, name = "item 2", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null) to 300,
            Food(id = 30, name = "item 3", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null) to 500
        )
    }

    @Test
    fun `Add new food, map contains added food and size increases by 1`() {
        val newFood = Food(id = 155, name = "some", protein = 11.6, fat = 57.0, carbs = 3.0, imageUrl = "some_uri")
        val updatedMap = useCase.addFood(newFood, 250, initialMap)
        assertThat(updatedMap).containsKey(newFood)
        assertThat(updatedMap.size-initialMap.size).isEqualTo(1)
    }

    @Test
    fun `Add already existing food, initial food gets replaced and size stays at initial value`() {
        val newFood = Food(id = 25, name = "item 2", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null)
        val newMass = 333
        val updatedMap = useCase.addFood(newFood, newMass, initialMap)
        assertThat(updatedMap).containsKey(newFood)
        assertThat(updatedMap[newFood]).isEqualTo(newMass)
        assertThat(updatedMap.size-initialMap.size).isEqualTo(0)
    }

    @Test
    fun `Delete food, map doesn't contain this food and size decreases by 1`() {
        val food = Food(id = 25, name = "item 2", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null)
        val updatedMap = useCase.deleteFood(food, initialMap)
        assertThat(updatedMap).doesNotContainKey(food)
        assertThat(updatedMap.size-initialMap.size).isEqualTo(-1)
    }

    @Test
    fun `Pick food that is already in picked list, get food with its mass`() {
        val food = Food(id = 25, name = "item 2", protein = 4.0, fat = 2.5, carbs = 3.0, imageUrl = null)
        val pair = useCase.getFood(food, initialMap)
        assertThat(pair.first).isEqualTo(food)
        assertThat(pair.second).isEqualTo(300)
    }

    @Test
    fun `Pick food that is not in picked list, get food with null mass`() {
        val food = Food(id = 155, name = "some", protein = 11.6, fat = 57.0, carbs = 3.0, imageUrl = "some_uri")
        val pair = useCase.getFood(food, initialMap)
        assertThat(pair.first).isEqualTo(food)
        assertThat(pair.second).isEqualTo(null)
    }
}