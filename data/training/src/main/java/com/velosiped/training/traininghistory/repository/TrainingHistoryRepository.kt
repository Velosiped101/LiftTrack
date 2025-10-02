package com.velosiped.training.traininghistory.repository

import kotlinx.coroutines.flow.Flow

interface TrainingHistoryRepository {

    suspend fun insertToTrainingHistory(items: List<TrainingHistory>)

    fun getAllTrainingHistory(): Flow<List<TrainingHistory>>

    fun getLatestWeightDone(exercise: String): Flow<Float>

    fun getWeightIncreaseHint(exercise: String): Flow<Boolean>

}