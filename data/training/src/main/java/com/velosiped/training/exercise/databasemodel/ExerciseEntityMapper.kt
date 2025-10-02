package com.velosiped.training.exercise.databasemodel

import com.velosiped.training.exercise.repository.Exercise

internal fun ExerciseEntity.toExercise() = Exercise(
    name = this.name,
    type = this.type
)