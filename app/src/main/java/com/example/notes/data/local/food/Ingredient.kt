package com.example.notes.data.local.food

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ingredient(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double
)