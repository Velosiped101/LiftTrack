package com.velosiped.notes.data.database.saveddata.mealhistory

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MealHistory(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val time: String,
    val name: String,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val mass: Int
)