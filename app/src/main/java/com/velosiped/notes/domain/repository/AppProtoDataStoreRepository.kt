package com.velosiped.notes.domain.repository

import com.velosiped.notes.presentation.screens.settingsScreen.SettingsUiState
import com.velosiped.notes.proto.AppProtoStore
import com.velosiped.notes.proto.ProtoProgramProgress
import kotlinx.coroutines.flow.Flow

interface AppProtoDataStoreRepository {
    val appProtoStoreFlow: Flow<AppProtoStore>

    suspend fun saveTempProgramProgress(progress: List<ProtoProgramProgress>)

    suspend fun finishProgramExecution()

    suspend fun liftProgramExecutionLock()

    suspend fun updateSettings(settings: SettingsUiState)

    suspend fun resetProgramProgress()
}