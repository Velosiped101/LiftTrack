package com.velosiped.notes.data.repository.exercise

import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.data.database.exercise.ExerciseDao
import com.velosiped.notes.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(
    private val dao: ExerciseDao
): ExerciseRepository {
    override fun getAll(): Flow<List<Exercise>> {
        return dao.getAll()
    }
}