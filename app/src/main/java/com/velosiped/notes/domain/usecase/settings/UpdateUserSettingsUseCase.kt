package com.velosiped.notes.domain.usecase.settings

import com.velosiped.notes.domain.repository.AppProtoDataStoreRepository
import com.velosiped.notes.presentation.screens.settingsScreen.SettingsUiState
import javax.inject.Inject

class UpdateUserSettingsUseCase @Inject constructor(
    private val repository: AppProtoDataStoreRepository
) {
    suspend operator fun invoke(state: SettingsUiState) {
        repository.updateSettings(state)
    }
}