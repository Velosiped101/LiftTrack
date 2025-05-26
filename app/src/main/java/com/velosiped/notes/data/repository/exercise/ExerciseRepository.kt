package com.velosiped.notes.data.repository.exercise

import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.data.database.exercise.ExerciseDao
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(
    private val dao: ExerciseDao
) {
    fun getAll(): Flow<List<Exercise>> {
        return dao.getAll()
    }
}