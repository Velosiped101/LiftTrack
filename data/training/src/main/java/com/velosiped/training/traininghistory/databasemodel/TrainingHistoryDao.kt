package com.velosiped.training.traininghistory.databasemodel

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TrainingHistoryDao {
    @Query("SELECT * from TrainingHistoryEntity")
    fun getAll(): Flow<List<TrainingHistoryEntity>>

    @Insert
    suspend fun insert(trainingHistory: List<TrainingHistoryEntity>)
}