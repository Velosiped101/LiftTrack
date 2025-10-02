package com.velosiped.diet.food.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    fun getAllDatabaseItems(): Flow<List<Food>>

    suspend fun deleteFromDatabase(list: List<Food>)

    suspend fun insertToDatabase(item: Food)

    fun getFoodApiPage(name: String, searchInLocal: Boolean): Flow<PagingData<Food>>

}