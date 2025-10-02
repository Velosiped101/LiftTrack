package com.velosiped.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.velosiped.datastore.DataStoreRepository
import com.velosiped.diet.mealhistory.repository.MealHistoryRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
internal class DailyResetWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val mealHistoryRepository: MealHistoryRepository,
    private val dataStoreRepository: DataStoreRepository
): CoroutineWorker(appContext,workerParameters) {
    override suspend fun doWork(): Result {
        try {
            mealHistoryRepository.clearMealHistory()
            dataStoreRepository.finishProgramExecution()
            dataStoreRepository.liftProgramExecutionLock()
            return Result.success()
        } catch (e: Exception) {
            Log.e(LOG_TAG, e.message.toString())
            return Result.failure()
        }
    }

    companion object {
        const val LOG_TAG = "Daily reset worker"
    }
}