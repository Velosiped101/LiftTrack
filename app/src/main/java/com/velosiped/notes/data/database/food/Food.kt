package com.velosiped.notes.data.database.food

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val protein: Double,
    val fat: Double,
    val carbs: Double,
    val imageUrl: String?
)