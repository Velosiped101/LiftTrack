package com.velosiped.diet.food.databasemodel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class FoodEntity(
    @PrimaryKey(autoGenerate = true) val id: Long? = null,
    val name: String,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val imageUrl: String? = null
)