package com.velosiped.notes.presentation.screens.settingsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.domain.usecase.settings.SettingsUseCase
import com.velosiped.notes.utils.CalorieSurplus
import com.velosiped.notes.utils.DailyActivityK
import com.velosiped.notes.utils.Sex
import com.velosiped.notes.utils.ThemeMode
import com.velosiped.notes.utils.toAppThemeMode
import com.velosiped.notes.utils.toCalorieSurplus
import com.velosiped.notes.utils.toDailyActivityK
import com.velosiped.notes.utils.toSex
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val useCase: SettingsUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
        val uiState = _uiState.asStateFlow()
    fun uiAction(action: SettingsUiAction) {
        when (action) {
            is SettingsUiAction.ChangeBodyMass -> changeBodyMass(action.mass)
            is SettingsUiAction.ChangeTheme -> changeTheme(action.theme)
            is SettingsUiAction.ChangeAge -> changeAge(action.age)
            is SettingsUiAction.ChangeCalorieSurplus -> changeCalorieSurplus(action.calorieSurplus)
            is SettingsUiAction.ChangeDailyActivity -> changeDailyActivityK(action.dailyActivityK)
            is SettingsUiAction.ChangeHeight -> changeHeight(action.height)
            is SettingsUiAction.ChangeSex -> changeSex(action.sex)
            SettingsUiAction.ConfirmSettings -> confirmSettings()
            is SettingsUiAction.ChangeResetTimeHour -> changeResetTimeHour(action.hour)
            is SettingsUiAction.ChangeResetTimeMinute -> changeResetTimeMinute(action.minute)
            SettingsUiAction.RestoreState -> restoreState()
        }
    }

    private var initialState: SettingsUiState? = null

    init {
        viewModelScope.launch {
            useCase.getUserSettingsUseCase().collect { data ->
                val prefs = data.appPreferences
                val state = if (prefs.isNotFirstLaunch) SettingsUiState().copy(
                    appThemeMode = prefs.themeMode.toAppThemeMode(),
                    bodyMass = prefs.bodyMass,
                    height = prefs.height,
                    age = prefs.age,
                    sex = prefs.sex.toSex(),
                    dailyActivityK = prefs.dailyActivityK.toDailyActivityK(),
                    calorieSurplus = prefs.calorieSurplus.toCalorieSurplus(),
                    resetTimeHour = prefs.resetTimeHour,
                    resetTimeMinute = prefs.resetTimeMinute,
                    isNotFirstLaunch = prefs.isNotFirstLaunch
                ) else SettingsUiState(isNotFirstLaunch = false)
                initialState = state
                _uiState.update {
                    state
                }
            }
        }
    }

    private fun changeTheme(theme: ThemeMode) {
        _uiState.update {
            it.copy(appThemeMode = theme)
        }
    }

    private fun changeBodyMass(mass: Float) {
        _uiState.update {
            it.copy(bodyMass = mass)
        }
    }

    private fun changeHeight(height: Int) {
        _uiState.update {
            it.copy(height = height)
        }
    }

    private fun changeAge(age: Int) {
        _uiState.update {
            it.copy(age = age)
        }
    }

    private fun changeSex(sex: Sex) {
        _uiState.update {
            it.copy(sex = sex)
        }
    }

    private fun changeDailyActivityK(dailyActivityK: DailyActivityK) {
        _uiState.update {
            it.copy(dailyActivityK = dailyActivityK)
        }
    }

    private fun changeCalorieSurplus(calorieSurplus: CalorieSurplus) {
        _uiState.update {
            it.copy(calorieSurplus = calorieSurplus)
        }
    }

    private fun confirmSettings() {
        viewModelScope.launch { useCase.updateUserSettingsUseCase(_uiState.value) }
    }

    private fun changeResetTimeHour(hour: Int) {
        _uiState.update {
            it.copy(resetTimeHour = hour)
        }
    }

    private fun changeResetTimeMinute(minute: Int) {
        _uiState.update {
            it.copy(resetTimeMinute = minute)
        }
    }

    private fun restoreState() {
        initialState?.let { restoredState ->
            _uiState.update {
                restoredState
            }
        }
    }
}