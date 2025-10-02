package com.velosiped.programexecution.utils

import com.velosiped.notes.data.datastore.ProtoProgramProgress
import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.utility.datehelper.DateHelper

fun TrainingHistory.toDataStoreTrainingHistory() = ProtoProgramProgress
    .newBuilder()
    .setDay(this.day)
    .setMonth(this.month)
    .setYear(this.year)
    .setExercise(this.exercise)
    .setReps(this.reps)
    .setRepsPlanned(this.repsPlanned)
    .setWeight(this.weight)
    .build()

fun ProtoProgramProgress.toTrainingHistory() = TrainingHistory(
    id = null,
    day = DateHelper.getCurrentDay(),
    month = DateHelper.getCurrentMonth(),
    year = DateHelper.getCurrentYear(),
    exercise = this.exercise,
    reps = this.reps,
    repsPlanned = this.repsPlanned,
    weight = this.weight
)