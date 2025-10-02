package com.velosiped.diet.food.datasource.api.foodApi

import androidx.core.net.toUri
import com.velosiped.diet.food.repository.Food

internal fun List<FoodApiResponse.Product>?.toFoodList(): List<Food> {
    return this?.filterNotNull()?.filter {
        it.productName != null &&
        it.productName.isNotBlank() &&
        it.nutriments != null &&
        it.nutriments.proteins != null &&
        it.nutriments.fat != null &&
        it.nutriments.carbohydrates != null
    }!!.map {
        Food(
            id = null,
            name = it.productName!!,
            protein = it.nutriments!!.proteins!!,
            fat = it.nutriments.fat!!,
            carbs = it.nutriments.carbohydrates!!,
            imageUri = it.imageUrl?.toUri()
        )
    }
}