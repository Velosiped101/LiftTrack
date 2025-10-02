package com.velosiped.statistic.domain

import com.velosiped.training.traininghistory.repository.TrainingHistoryRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetExercisesFromHistoryUseCase @Inject constructor(
    private val trainingHistoryRepository: TrainingHistoryRepository
) {
    suspend operator fun invoke(): List<String> {
        return trainingHistoryRepository.getAllTrainingHistory().first()
            .map { it.exercise }
            .toSet()
            .toList()
    }
}