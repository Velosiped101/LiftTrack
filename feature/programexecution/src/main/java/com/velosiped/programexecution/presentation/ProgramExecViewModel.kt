package com.velosiped.programexecution.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.programexecution.domain.usecase.FinishTrainingUseCase
import com.velosiped.programexecution.domain.usecase.GetTrainingStateUseCase
import com.velosiped.programexecution.domain.usecase.UpdateStoredProgressUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramExecViewModel @Inject constructor(
    private val getTrainingStateUseCase: GetTrainingStateUseCase,
    private val finishTrainingUseCase: FinishTrainingUseCase,
    private val updateStoredProgressUseCase: UpdateStoredProgressUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<ProgramExecUiState> = MutableStateFlow(ProgramExecUiState())
        val uiState = _uiState.asStateFlow()
    
    private val _saveCompleted: MutableSharedFlow<Unit> = MutableSharedFlow()
        val saveCompleted = _saveCompleted.asSharedFlow()

    init {
        viewModelScope.launch {
            val updatedState = getTrainingStateUseCase()
            _uiState.update { updatedState }
        }
    }

    fun onRepsChange(reps: Float, index: Int) {
        _uiState.update {
            val updatedProgramProgress = it.trainingHistory.mapIndexed { id, programProgress ->
                if (id == index) programProgress.copy(reps = reps.toInt())
                else programProgress
            }
            it.copy(trainingHistory = updatedProgramProgress)
        }
    }

    fun onWeightChange(weight: Float, index: Int) {
        _uiState.update {
            val updatedProgramProgress = it.trainingHistory.mapIndexed { id, programProgress ->
                if (id == index) programProgress.copy(weight = weight)
                else programProgress
            }
            it.copy(trainingHistory = updatedProgramProgress)
        }
    }

    fun onConfirm() {
        viewModelScope.launch {
            finishTrainingUseCase(_uiState.value.trainingHistory)
            _saveCompleted.emit(Unit)
        }
    }

    fun onUpdateStoredProgress() {
        viewModelScope.launch {
            updateStoredProgressUseCase(_uiState.value.trainingHistory)
        }
    }
}