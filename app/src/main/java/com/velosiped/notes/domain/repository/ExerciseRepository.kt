package com.velosiped.notes.domain.repository

import com.velosiped.notes.data.database.exercise.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getAll(): Flow<List<Exercise>>
}