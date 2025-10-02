package com.velosiped.training.traininghistory.databasemodel

import com.velosiped.training.traininghistory.repository.TrainingHistory

internal fun TrainingHistoryEntity.toTrainingHistory() = TrainingHistory(
    id = this.id,
    day = this.day,
    month = this.month,
    year = this.year,
    exercise = this.exercise,
    reps = this.reps,
    repsPlanned = this.repsPlanned,
    weight = this.weight
)

internal fun TrainingHistory.toTrainingHistoryEntity() = TrainingHistoryEntity(
    id = this.id,
    day = this.day,
    month = this.month,
    year = this.year,
    exercise = this.exercise,
    reps = this.reps,
    repsPlanned = this.repsPlanned,
    weight = this.weight
)