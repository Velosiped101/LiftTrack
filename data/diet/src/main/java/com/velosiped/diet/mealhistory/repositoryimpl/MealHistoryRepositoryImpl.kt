package com.velosiped.diet.mealhistory.repositoryimpl

import com.velosiped.diet.mealhistory.databasemodel.MealHistoryDao
import com.velosiped.diet.mealhistory.databasemodel.toMealHistory
import com.velosiped.diet.mealhistory.databasemodel.toMealHistoryEntity
import com.velosiped.diet.mealhistory.repository.MealHistory
import com.velosiped.diet.mealhistory.repository.MealHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MealHistoryRepositoryImpl(
    private val mealHistoryDao: MealHistoryDao
): MealHistoryRepository {
    override suspend fun insertToHistory(elements: List<MealHistory>) {
        mealHistoryDao.insert(elements.map { it.toMealHistoryEntity() })
    }

    override fun getMealHistory(): Flow<List<MealHistory>> {
        return mealHistoryDao.getAll().map { it.map { it.toMealHistory() } }
    }

    override suspend fun clearMealHistory() {
        mealHistoryDao.clear()
    }

}