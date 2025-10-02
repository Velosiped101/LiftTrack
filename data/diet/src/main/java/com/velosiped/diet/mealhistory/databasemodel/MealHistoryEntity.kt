package com.velosiped.diet.mealhistory.databasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class MealHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val time: String,
    val name: String,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val mass: Int
)