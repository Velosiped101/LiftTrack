package com.velosiped.datastore

import com.velosiped.notes.data.datastore.AppPreferences
import com.velosiped.notes.data.datastore.AppProtoStore
import com.velosiped.notes.data.datastore.ProtoProgramProgress
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val appProtoStoreFlow: Flow<AppProtoStore>

    suspend fun saveTempTrainingHistory(progress: List<ProtoProgramProgress>)

    suspend fun finishProgramExecution()

    suspend fun liftProgramExecutionLock()

    suspend fun updatePreferences(preferences: AppPreferences)

    suspend fun resetProgramProgress()
}