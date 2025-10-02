package com.velosiped.programexecution.domain.usecase

import com.velosiped.datastore.DataStoreRepository
import com.velosiped.programexecution.presentation.ProgramExecUiState
import com.velosiped.programexecution.utils.toDataStoreTrainingHistory
import com.velosiped.programexecution.utils.toTrainingHistory
import com.velosiped.training.program.repository.ProgramRepository
import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.training.traininghistory.repository.TrainingHistoryRepository
import com.velosiped.utility.datehelper.DateHelper
import com.velosiped.utility.extensions.ZERO
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetTrainingStateUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val programRepository: ProgramRepository,
    private val trainingHistoryRepository: TrainingHistoryRepository
) {
    suspend operator fun invoke(): ProgramExecUiState {
        val savedData = dataStoreRepository.appProtoStoreFlow.first()
        if (savedData.protoProgramProgressItemsList.isEmpty()) {
            val program = programRepository.getAllProgramItems().first()
            val trainingState = program.map {
                val weight = trainingHistoryRepository.getLatestWeightDone(it.exercise).first()
                val showHint = trainingHistoryRepository.getWeightIncreaseHint(it.exercise).first()
                Pair(
                    TrainingHistory(
                        day = DateHelper.getCurrentDay(),
                        month = DateHelper.getCurrentMonth(),
                        year = DateHelper.getCurrentYear(),
                        reps = Int.ZERO,
                        exercise = it.exercise,
                        repsPlanned = it.reps,
                        weight = weight
                    ), showHint
                )
            }
            val trainingHistory = trainingState.map { it.first }
            val showHintList = trainingState.map { it.second }
            val initialWeight = trainingHistory.map { it.weight }
            dataStoreRepository.saveTempTrainingHistory(trainingHistory.map { it.toDataStoreTrainingHistory() })
            return ProgramExecUiState(trainingHistory, showHintList, initialWeight)
        } else {
            val programProgress = savedData.protoProgramProgressItemsList.map { it.toTrainingHistory() }
            val initialWeight = savedData.protoProgramProgressItemsList.map {
                trainingHistoryRepository.getLatestWeightDone(it.exercise).first()
            }
            val showHintList = savedData.protoProgramProgressItemsList.map {
                trainingHistoryRepository.getWeightIncreaseHint(it.exercise).first()
            }
            return ProgramExecUiState(programProgress, showHintList, initialWeight)
        }
    }
}