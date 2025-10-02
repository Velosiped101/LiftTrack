package com.velosiped.diet.food.datasource.api.foodApi

import com.google.gson.annotations.SerializedName

internal data class FoodApiResponse(
    @SerializedName("products") val products: List<Product>?,
    @SerializedName("page") val page: Int,
) {
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
}