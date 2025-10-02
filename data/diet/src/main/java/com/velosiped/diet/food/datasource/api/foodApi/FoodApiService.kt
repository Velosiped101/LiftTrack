package com.velosiped.diet.food.datasource.api.foodApi

import retrofit2.http.GET
import retrofit2.http.Query

internal interface FoodApiService {
    @GET(FoodApiConstants.BASE_URL_ENDPOINT)
    suspend fun getFood(
        @Query("search_terms") searchTerms: String,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int = 10,
        @Query("fields") fields: String = "product_name,nutriments,image_url",
        @Query("sort_by") sortBy: String = "popularity_key",
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1
    ): FoodApiResponse
}