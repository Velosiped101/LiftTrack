package com.velosiped.notes.domain.usecase.training.programedit

import com.velosiped.notes.data.database.exercise.Exercise
import com.velosiped.notes.data.repository.exercise.ExerciseRepositoryImpl
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetExerciseListUseCase @Inject constructor(
    private val exerciseRepositoryImpl: ExerciseRepositoryImpl
) {
    suspend operator fun invoke(): List<Exercise> {
        return exerciseRepositoryImpl.getAll().first()
    }
}