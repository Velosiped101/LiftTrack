package com.velosiped.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.datastore.DataStoreRepository
import com.velosiped.settings.presentation.utils.DailyActivity
import com.velosiped.settings.presentation.utils.Goal
import com.velosiped.settings.presentation.utils.ResetTime
import com.velosiped.settings.presentation.utils.Sex
import com.velosiped.ui.utils.ThemeMode
import com.velosiped.settings.presentation.utils.mappers.toAppPreferences
import com.velosiped.settings.presentation.utils.mappers.toAppThemeMode
import com.velosiped.settings.presentation.utils.mappers.toSettingsUiState
import com.velosiped.worker.WorkScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val workScheduler: WorkScheduler
): ViewModel() {
    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
        val uiState = _uiState.asStateFlow()

    private val _settingsSaved = MutableSharedFlow<Unit>()
        val settingsSaved = _settingsSaved.asSharedFlow()

    private var initialState: SettingsUiState? = null

    private val _themeMode: MutableStateFlow<ThemeMode> = MutableStateFlow(ThemeMode.SYSTEM)
        val themeMode = _themeMode.asStateFlow()

    private val _isNotFirstLaunch: MutableStateFlow<Boolean?> = MutableStateFlow(null)
        val isNotFirstLaunch = _isNotFirstLaunch.asStateFlow()

    init {
        viewModelScope.launch {
            dataStoreRepository.appProtoStoreFlow.collect { data ->
                val prefs = data.appPreferences

                _themeMode.emit(prefs.themeMode.toAppThemeMode())
                _isNotFirstLaunch.emit(prefs.isNotFirstLaunch)

                val state =
                    if (prefs.isNotFirstLaunch) prefs.toSettingsUiState()
                    else SettingsUiState(isNotFirstLaunch = false)
                initialState = state
                _uiState.update { state }

                workScheduler.initWorker(
                    resetTimeHour = uiState.value.resetTime.hour,
                    resetTimeMinute = uiState.value.resetTime.minute
                )
            }
        }
    }

    fun changeTheme(theme: ThemeMode) {
        _uiState.update {
            it.copy(appThemeMode = theme)
        }
    }

    fun changeBodyMass(mass: Float) {
        _uiState.update {
            it.copy(bodyMass = mass)
        }
    }

    fun changeHeight(height: Int) {
        _uiState.update {
            it.copy(height = height)
        }
    }

    fun changeAge(age: Int) {
        _uiState.update {
            it.copy(age = age)
        }
    }

    fun changeSex(sex: Sex) {
        _uiState.update {
            it.copy(sex = sex)
        }
    }

    fun changeDailyActivityK(dailyActivity: DailyActivity) {
        _uiState.update {
            it.copy(dailyActivity = dailyActivity)
        }
    }

    fun changeGoal(goal: Goal) {
        _uiState.update {
            it.copy(goal = goal)
        }
    }

    fun confirmSettings() {
        viewModelScope.launch {
            dataStoreRepository.updatePreferences(_uiState.value.toAppPreferences())
            _settingsSaved.emit(Unit)
        }
    }

    fun changeResetTimeHour(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(resetTime = ResetTime(hour, minute))
        }
    }

    fun restoreState() {
        initialState?.let { restoredState ->
            _uiState.update {
                restoredState
            }
        }
    }
}