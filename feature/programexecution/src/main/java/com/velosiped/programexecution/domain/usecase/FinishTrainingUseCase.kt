package com.velosiped.programexecution.domain.usecase

import com.velosiped.datastore.DataStoreRepository
import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.training.traininghistory.repository.TrainingHistoryRepository
import jakarta.inject.Inject

class FinishTrainingUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val trainingHistoryRepository: TrainingHistoryRepository
) {
    suspend operator fun invoke(trainingProgress: List<TrainingHistory>) {
        trainingHistoryRepository.insertToTrainingHistory(trainingProgress)
        dataStoreRepository.finishProgramExecution()
    }
}