package com.velosiped.diet.ingredient.databasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class IngredientEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,
    val protein: Float,
    val fat: Float,
    val carbs: Float
)