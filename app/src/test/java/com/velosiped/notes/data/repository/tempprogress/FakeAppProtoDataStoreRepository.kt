package com.velosiped.notes.data.repository.tempprogress

import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsUiState
import com.velosiped.notes.proto.AppProtoStore
import com.velosiped.notes.proto.ProtoProgramProgress
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppProtoDataStoreRepository: AppProtoDataStoreRepository {
    private val _appProtoStoreFlow = MutableStateFlow(AppProtoStore.getDefaultInstance())

    override val appProtoStoreFlow: Flow<AppProtoStore>
        get() = _appProtoStoreFlow

    override suspend fun saveTempProgramProgress(progress: List<ProtoProgramProgress>) {
        _appProtoStoreFlow.emit(
            AppProtoStore.newBuilder().addAllProtoProgramProgressItems(progress).build()
        )
    }

    override suspend fun finishProgramExecution() {
        TODO("Not yet implemented")
    }

    override suspend fun liftProgramExecutionLock() {
        TODO("Not yet implemented")
    }

    override suspend fun updateSettings(settings: SettingsUiState) {
        TODO("Not yet implemented")
    }

    override suspend fun resetProgramProgress() {
        TODO("Not yet implemented")
    }

}