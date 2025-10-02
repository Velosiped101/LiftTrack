package com.velosiped.worker

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkScheduler @Inject constructor(
    private val workManager: WorkManager
) {
    fun initWorker(resetTimeHour: Int, resetTimeMinute: Int) {
        val delay = getWorkDelay(resetHour = resetTimeHour, resetMinute = resetTimeMinute)
        val dailyResetRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<DailyResetWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()
        workManager
            .enqueueUniqueWork(
                DAILY_RESET_WORKER_NAME,
                ExistingWorkPolicy.REPLACE,
                dailyResetRequest
            )
    }

    private fun getWorkDelay(resetHour: Int, resetMinute: Int): Long {
        val currentTime = Calendar.getInstance().timeInMillis
        val workExecutionTime = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, resetHour)
            set(Calendar.MINUTE, resetMinute)
            set(Calendar.SECOND, 0)
        }.timeInMillis
        var delay = workExecutionTime - currentTime
        if (delay < 0) delay += 24 * 60 * 60 * 1000
        return delay
    }

    companion object {
        const val DAILY_RESET_WORKER_NAME = "daily_reset"
    }
}