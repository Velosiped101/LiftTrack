package com.velosiped.notes.domain.usecase.main

import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.domain.repository.ProgramRepository
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CheckForProgramUpdateUseCase @Inject constructor(
    private val programRepository: ProgramRepository,
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository
) {
    suspend operator fun invoke(): Boolean {
        return combine(
            programRepository.getProgramForToday(),
            appProtoDataStoreRepository.appProtoStoreFlow
        ){ programList, dataStore ->
            val savedProgress = dataStore.protoProgramProgressItemsList
            if (savedProgress.isEmpty()) return@combine false
            val differentSize = (programList.size != savedProgress.size)
            if (differentSize) return@combine true
            val differentExercises = programList.zip(savedProgress).any { (program, savedProgram) ->
                program.exercise != savedProgram.exercise
            }
            val differentRepsPlanned = programList.zip(savedProgress).any { (program, savedProgram) ->
                program.reps != savedProgram.repsPlanned
            }
            differentExercises || differentRepsPlanned
        }.first()
    }
}