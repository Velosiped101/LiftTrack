package com.velosiped.diet.ingredient.databasemodel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface IngredientDao {
    @Query("SELECT * FROM IngredientEntity WHERE name LIKE '%' || :ingredientName || '%'")
    fun getIngredients(ingredientName: String): Flow<List<IngredientEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)
}