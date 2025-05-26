package com.velosiped.notes.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.velosiped.notes.data.repository.diet.DietRepository
import com.velosiped.notes.data.repository.tempProgress.AppProtoDataStoreRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyResetWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParameters: WorkerParameters,
    private val dietRepository: DietRepository,
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository
): CoroutineWorker(appContext,workerParameters) {
    override suspend fun doWork(): Result {
        Log.e("DoWork", "doWork() started")
        try {
            dietRepository.clearMealHistory()
            appProtoDataStoreRepository.finishProgramExecution()
            appProtoDataStoreRepository.liftProgramExecutionLock()
            Log.e("DoWork", "success")
            return Result.success()
        } catch (e: Exception) {
            Log.e("DoWork", "error: ${e.cause}")
            return Result.failure()
        }
    }
}