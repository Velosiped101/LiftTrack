package com.velosiped.datastore.implementation

import androidx.datastore.core.DataStore
import com.velosiped.datastore.DataStoreRepository
import com.velosiped.notes.data.datastore.AppPreferences
import com.velosiped.notes.data.datastore.AppProtoStore
import com.velosiped.notes.data.datastore.ProtoProgramProgress
import kotlinx.coroutines.flow.catch
import java.io.IOException

internal class DataStoreRepositoryImpl(
    private val dataStore: DataStore<AppProtoStore>
): DataStoreRepository {
    override val appProtoStoreFlow = dataStore.data.catch {
        if (it is IOException) {
            emit(AppProtoStore.getDefaultInstance())
        } else throw Exception()
    }

    override suspend fun saveTempTrainingHistory(progress: List<ProtoProgramProgress>) {
        dataStore.updateData {
            it
                .toBuilder()
                .clearProtoProgramProgressItems()
                .addAllProtoProgramProgressItems(progress)
                .build()
        }
    }

    override suspend fun finishProgramExecution() {
        dataStore.updateData { appProtoStore ->
            val updatedLock = appProtoStore.programExecLock.toBuilder().setProgramExecutionLocked(true).build()
            appProtoStore
                .toBuilder()
                .clearProtoProgramProgressItems()
                .setProgramExecLock(updatedLock)
                .build()
        }
    }

    override suspend fun liftProgramExecutionLock() {
        dataStore.updateData { appProtoStore ->
            val updatedLock = appProtoStore.programExecLock.toBuilder().setProgramExecutionLocked(false).build()
            appProtoStore
                .toBuilder()
                .setProgramExecLock(updatedLock)
                .build()
        }
    }

    override suspend fun updatePreferences(preferences: AppPreferences) {
        dataStore.updateData {
            it.toBuilder().setAppPreferences(preferences).build()
        }
    }

    override suspend fun resetProgramProgress() {
        dataStore.updateData { appProtoStore ->
            appProtoStore
                .toBuilder()
                .clearProtoProgramProgressItems()
                .build()
        }
    }
}