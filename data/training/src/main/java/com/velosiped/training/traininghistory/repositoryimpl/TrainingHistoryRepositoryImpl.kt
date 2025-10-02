package com.velosiped.training.traininghistory.repositoryimpl

import com.velosiped.training.traininghistory.databasemodel.TrainingHistoryDao
import com.velosiped.training.traininghistory.databasemodel.toTrainingHistory
import com.velosiped.training.traininghistory.databasemodel.toTrainingHistoryEntity
import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.training.traininghistory.repository.TrainingHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class TrainingHistoryRepositoryImpl(
    private val trainingHistoryDao: TrainingHistoryDao
): TrainingHistoryRepository {
    override suspend fun insertToTrainingHistory(items: List<TrainingHistory>) {
        trainingHistoryDao.insert(items.map { it.toTrainingHistoryEntity() })
    }

    override fun getAllTrainingHistory(): Flow<List<TrainingHistory>> {
        return trainingHistoryDao.getAll().map { it.map { it.toTrainingHistory() } }
    }

    override fun getLatestWeightDone(exercise: String): Flow<Float> {
        return trainingHistoryDao.getAll().map { list ->
            list
                .groupBy { it.exercise }
                .filter { it.key == exercise }
                .values
                .lastOrNull()
                ?.groupBy { Triple(it.day, it.month, it.year) }
                ?.values
                ?.lastOrNull()
                ?.minOf { it.weight } ?: 0f
        }
    }

    override fun getWeightIncreaseHint(exercise: String): Flow<Boolean> {
        return trainingHistoryDao.getAll().map { list ->
            list
                .groupBy { it.exercise }
                .filter { it.key == exercise }
                .values
                .lastOrNull()
                ?.groupBy { Triple(it.day, it.month, it.year) }
                ?.values
                ?.lastOrNull()
                ?.all { it.reps >= it.repsPlanned } == true
        }
    }
}