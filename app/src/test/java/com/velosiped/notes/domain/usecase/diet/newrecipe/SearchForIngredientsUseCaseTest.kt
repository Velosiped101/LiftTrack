package com.velosiped.notes.domain.usecase.diet.newrecipe

import com.google.common.truth.Truth.assertThat
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.data.repository.diet.FakeDietRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class SearchForIngredientsUseCaseTest {
    private lateinit var getIngredients: SearchForIngredientsUseCase
    private lateinit var repository: FakeDietRepository

    @Before
    fun setup() {
        repository = FakeDietRepository()
        getIngredients = SearchForIngredientsUseCase(repository)
    }

    @Test
    fun `Search query with length shorter than 3, get empty flow`(): Unit = runBlocking {
        val ingredientsList = listOf(
            Ingredient(name = "carrot", protein = 1.0, fat = 0.0, carbs = 5.0),
            Ingredient(name = "apple", protein = 2.0, fat = 2.0, carbs = 26.0),
            Ingredient(name = "rabbit", protein = 29.0, fat = 23.0, carbs = 10.0)
        )
        ingredientsList.forEach { repository.insertIngredient(it) }
        val ingredientsFound = getIngredients("ca").first()
        assertThat(ingredientsFound).isEmpty()
    }

    @Test
    fun `Search query with length 3 or greater and matches, get flow of matching ingredients`(): Unit = runBlocking {
        val ingredientsList = listOf(
            Ingredient(name = "carrot", protein = 1.0, fat = 0.0, carbs = 5.0),
            Ingredient(name = "apple", protein = 2.0, fat = 2.0, carbs = 26.0),
            Ingredient(name = "rabbit", protein = 29.0, fat = 23.0, carbs = 10.0),
            Ingredient(name = "not_car_what", protein = 29.0, fat = 23.0, carbs = 10.0),
            Ingredient(name = "nascar", protein = 29.0, fat = 23.0, carbs = 10.0),
        )
        ingredientsList.forEach { repository.insertIngredient(it) }
        val ingredientsFound = getIngredients("car").first()
        assertThat(ingredientsFound).hasSize(3)
    }

    @Test
    fun `Search query with length 3 or greater and no matches, get empty flow`(): Unit = runBlocking {
        val ingredientsList = listOf(
            Ingredient(name = "carrot", protein = 1.0, fat = 0.0, carbs = 5.0),
            Ingredient(name = "apple", protein = 2.0, fat = 2.0, carbs = 26.0),
            Ingredient(name = "rabbit", protein = 29.0, fat = 23.0, carbs = 10.0)
        )
        ingredientsList.forEach { repository.insertIngredient(it) }
        val ingredientsFound = getIngredients("something unimaginable").first()
        assertThat(ingredientsFound).isEmpty()
    }
}