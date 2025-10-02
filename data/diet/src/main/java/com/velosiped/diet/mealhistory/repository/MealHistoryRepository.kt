package com.velosiped.diet.mealhistory.repository

import kotlinx.coroutines.flow.Flow

interface MealHistoryRepository {

    suspend fun insertToHistory(elements: List<MealHistory>)

    fun getMealHistory(): Flow<List<MealHistory>>

    suspend fun clearMealHistory()

}