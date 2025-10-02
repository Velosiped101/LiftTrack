package com.velosiped.training.exercise.repositoryimpl

import com.velosiped.training.exercise.databasemodel.ExerciseDao
import com.velosiped.training.exercise.databasemodel.toExercise
import com.velosiped.training.exercise.repository.Exercise
import com.velosiped.training.exercise.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ExerciseRepositoryImpl(
    private val dao: ExerciseDao
): ExerciseRepository {
    override fun getAllExercises(): Flow<List<Exercise>> {
        return dao.getAll().map { it.map { it.toExercise() } }
    }
}