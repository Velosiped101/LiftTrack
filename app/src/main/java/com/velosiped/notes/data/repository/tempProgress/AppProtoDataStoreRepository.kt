package com.velosiped.notes.data.repository.tempProgress

import androidx.datastore.core.DataStore
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsUiState
import com.velosiped.notes.proto.AppPreferences
import com.velosiped.notes.proto.AppProtoStore
import com.velosiped.notes.proto.ProtoProgramProgress
import com.velosiped.notes.utils.toProto
import kotlinx.coroutines.flow.catch
import java.io.IOException

class AppProtoDataStoreRepository(
    private val dataStore: DataStore<AppProtoStore>
) {
    val appProtoStoreFlow = dataStore.data.catch {
        if (it is IOException) {
            emit(AppProtoStore.getDefaultInstance())
        } else throw Exception()
    }

    suspend fun saveTempProgramProgress(progress: List<ProtoProgramProgress>) {
        dataStore.updateData {
            it
                .toBuilder()
                .clearProtoProgramProgressItems()
                .addAllProtoProgramProgressItems(progress)
                .build()
        }
    }

    suspend fun finishProgramExecution() {
        dataStore.updateData { appProtoStore ->
            val updatedLock = appProtoStore.programExecLock.toBuilder().setProgramExecutionLocked(true).build()
            appProtoStore
                .toBuilder()
                .clearProtoProgramProgressItems()
                .setProgramExecLock(updatedLock)
                .build()
        }
    }

    suspend fun liftProgramExecutionLock() {
        dataStore.updateData { appProtoStore ->
            val updatedLock = appProtoStore.programExecLock.toBuilder().setProgramExecutionLocked(false).build()
            appProtoStore
                .toBuilder()
                .setProgramExecLock(updatedLock)
                .build()
        }
    }

    suspend fun updateSettings(settings: SettingsUiState) {
        dataStore.updateData { appProtoStore ->
            val updatedPreferences = AppPreferences.newBuilder()
                .setThemeMode(settings.appThemeMode.toProto())
                .setBodyMass(settings.bodyMass)
                .setHeight(settings.height)
                .setAge(settings.age)
                .setSex(settings.sex.toProto())
                .setResetTimeHour(settings.resetTimeHour)
                .setResetTimeMinute(settings.resetTimeMinute)
                .setDailyActivityK(settings.dailyActivityK.toProto())
                .setCalorieSurplus(settings.calorieSurplus.toProto())
                .setTargetCalories(settings.autoTargetCalories)
                .setIsNotFirstLaunch(true)
                .build()
            appProtoStore.toBuilder().setAppPreferences(updatedPreferences).build()
        }
    }

    suspend fun resetProgramProgress() {
        dataStore.updateData { appProtoStore ->
            appProtoStore
                .toBuilder()
                .clearProtoProgramProgressItems()
                .build()
        }
    }

}