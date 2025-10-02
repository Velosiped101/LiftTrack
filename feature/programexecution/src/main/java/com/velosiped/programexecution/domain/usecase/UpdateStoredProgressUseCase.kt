package com.velosiped.programexecution.domain.usecase

import com.velosiped.datastore.DataStoreRepository
import com.velosiped.programexecution.utils.toDataStoreTrainingHistory
import com.velosiped.training.traininghistory.repository.TrainingHistory
import javax.inject.Inject

class UpdateStoredProgressUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(trainingHistory: List<TrainingHistory>) {
        dataStoreRepository.saveTempTrainingHistory(
            trainingHistory.map { it.toDataStoreTrainingHistory() }
        )
    }
}