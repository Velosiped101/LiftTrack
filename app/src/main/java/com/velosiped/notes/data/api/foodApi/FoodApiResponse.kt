package com.velosiped.notes.data.api.foodApi

import com.velosiped.notes.data.database.food.Food
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal
import java.math.RoundingMode

data class FoodApiResponse(
    @SerializedName("products") val products: List<Product>?,
    @SerializedName("page") val page: Int,
)

data class Product(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("nutriments") val nutriments: Nutriments?,
    @SerializedName("image_url") val imageUrl: String?
)

data class Nutriments(
    @SerializedName("fat") val fat: Float?,
    @SerializedName("proteins") val proteins: Float?,
    @SerializedName("carbohydrates") val carbohydrates: Float?,
)

fun List<Product>?.toFoodList(): List<Food> {
    return this?.filter { params ->
        listOf(
            params.productName,
            params.nutriments,
            params.nutriments?.proteins,
            params.nutriments?.fat,
            params.nutriments?.carbohydrates
        ).none { it == null }
    }?.map {
        Food(
            name = it.productName!!,
            protein = it.nutriments!!.proteins!!.toDouble().cut(),
            fat = it.nutriments.fat!!.toDouble().cut(),
            carbs = it.nutriments.carbohydrates!!.toDouble().cut(),
            imageUrl = it.imageUrl
        )
    } ?: listOf()
}

fun Double.cut(): Double {
    return BigDecimal(this).setScale(2, RoundingMode.HALF_UP).toDouble()
}