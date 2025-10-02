package com.velosiped.diet.mealhistory.databasemodel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface MealHistoryDao {
    @Query("SELECT * FROM MealHistoryEntity")
    fun getAll(): Flow<List<MealHistoryEntity>>

    @Insert
    suspend fun insert(items: List<MealHistoryEntity>)

    @Query("DELETE FROM MealHistoryEntity")
    suspend fun clear()
}