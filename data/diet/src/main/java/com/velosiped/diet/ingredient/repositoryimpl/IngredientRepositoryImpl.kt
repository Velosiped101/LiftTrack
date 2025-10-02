package com.velosiped.diet.ingredient.repositoryimpl

import com.velosiped.diet.ingredient.databasemodel.IngredientDao
import com.velosiped.diet.ingredient.databasemodel.toIngredient
import com.velosiped.diet.ingredient.databasemodel.toIngredientEntity
import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.diet.ingredient.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class IngredientRepositoryImpl(
    private val ingredientDao: IngredientDao
): IngredientRepository {
    override fun getIngredients(name: String): Flow<List<Ingredient>> {
        return ingredientDao.getIngredients(ingredientName = name).map { it.map { it.toIngredient() } }
    }

    override suspend fun insertIngredients(ingredients: List<Ingredient>) {
        ingredientDao.insertIngredients(ingredients.map { it.toIngredientEntity() })
    }

}