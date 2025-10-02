package com.velosiped.diet.food.repository

import android.net.Uri

data class Food(
    val id: Long? = null,
    val name: String,
    val protein: Float,
    val fat: Float,
    val carbs: Float,
    val imageUri: Uri?
)