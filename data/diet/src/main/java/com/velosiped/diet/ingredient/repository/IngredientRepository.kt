package com.velosiped.diet.ingredient.repository

import kotlinx.coroutines.flow.Flow

interface IngredientRepository {

    fun getIngredients(name: String): Flow<List<Ingredient>>

    suspend fun insertIngredients(ingredients: List<Ingredient>)

}