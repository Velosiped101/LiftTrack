package com.velosiped.notes.domain.usecase.training.programexec

import com.velosiped.notes.data.database.saveddata.programProgress.ProgramProgress
import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.domain.repository.ProgramRepository
import com.velosiped.notes.presentation.screens.training.programExecScreen.ProgramExecUiState
import com.velosiped.notes.utils.Date
import com.velosiped.notes.utils.toProgramProgressList
import com.velosiped.notes.utils.toProgramTempProgressItemsList
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTrainingStateUseCase @Inject constructor(
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository,
    private val programRepository: ProgramRepository
) {
    suspend operator fun invoke(): ProgramExecUiState {
        val savedData = appProtoDataStoreRepository.appProtoStoreFlow.first()
        if (savedData.protoProgramProgressItemsList.isEmpty()) {
            val program = programRepository.getProgramForToday().first()
            val programState = program.map {
                val weight = programRepository.getLatestWeightDone(it.exercise).first()
                val showHint = programRepository.getWeightIncreaseHint(it.exercise).first()
                Pair(
                    ProgramProgress(
                        day = Date.day,
                        month = Date.month,
                        year = Date.year,
                        reps = 0,
                        exercise = it.exercise,
                        repsPlanned = it.reps,
                        weight = weight
                    ), showHint
                )
            }
            val programProgress = programState.map { it.first }
            val showHintList = programState.map { it.second }
            val initialWeight = programProgress.map { it.weight }
            appProtoDataStoreRepository.saveTempProgramProgress(programProgress.toProgramTempProgressItemsList())
            return ProgramExecUiState(programProgress, showHintList, initialWeight)
        } else {
            val programProgress = savedData.protoProgramProgressItemsList.toProgramProgressList()
            val initialWeight = savedData.protoProgramProgressItemsList.map {
                programRepository.getLatestWeightDone(it.exercise).first()
            }
            val showHintList = savedData.protoProgramProgressItemsList.map {
                programRepository.getWeightIncreaseHint(it.exercise).first()
            }
            return ProgramExecUiState(programProgress, showHintList, initialWeight)
        }
    }
}