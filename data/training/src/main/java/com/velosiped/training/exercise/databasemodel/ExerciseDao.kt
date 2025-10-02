package com.velosiped.training.exercise.databasemodel

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
internal interface ExerciseDao {
    @Query("SELECT * FROM ExerciseEntity")
    fun getAll(): Flow<List<ExerciseEntity>>
}