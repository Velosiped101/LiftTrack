package com.example.notes.data.repository.tempProgress

import com.example.notes.data.database.saveddata.programProgress.ProgramProgress
import com.example.notes.proto.ProtoProgramProgress

fun List<ProtoProgramProgress>.toProgramProgressList(): List<ProgramProgress> {
    return this.map {
        ProgramProgress(
            day = it.day,
            month = it.month,
            year = it.year,
            exercise = it.exercise,
            reps = it.reps,
            repsPlanned = it.repsPlanned,
            weight = it.weight
        )
    }
}

fun List<ProgramProgress>.toProgramTempProgressItemsList(): List<ProtoProgramProgress> {
    return this.map {
        ProtoProgramProgress
            .newBuilder()
            .setDay(it.day)
            .setMonth(it.month)
            .setYear(it.year)
            .setExercise(it.exercise)
            .setReps(it.reps)
            .setRepsPlanned(it.repsPlanned)
            .setWeight(it.weight)
            .build()
    }
}