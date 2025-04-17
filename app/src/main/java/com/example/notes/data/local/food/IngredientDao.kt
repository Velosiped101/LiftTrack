package com.example.notes.data.local.food

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface IngredientDao {
    @Query("SELECT * FROM Ingredient WHERE name LIKE '%' || :ingredientName || '%'")
    suspend fun getIngredient(ingredientName: String): List<Ingredient>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredient(ingredient: Ingredient)
}