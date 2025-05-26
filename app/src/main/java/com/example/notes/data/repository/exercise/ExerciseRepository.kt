package com.example.notes.data.repository.exercise

import com.example.notes.data.database.exercise.Exercise
import com.example.notes.data.database.exercise.ExerciseDao
import kotlinx.coroutines.flow.Flow

class ExerciseRepository(
    private val dao: ExerciseDao
) {
    fun getAll(): Flow<List<Exercise>> {
        return dao.getAll()
    }
}