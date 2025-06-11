package com.velosiped.notes.presentation.screens.training.programExecScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.domain.usecase.training.TrainingUseCase
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
    private val useCase: TrainingUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<ProgramExecUiState> = MutableStateFlow(ProgramExecUiState())
        val uiState = _uiState.asStateFlow()
    fun uiAction(action: ProgramExecUiAction) {
        when (action) {
            is ProgramExecUiAction.ChangeRepsDone -> changeRepsDone(index = action.index, reps = action.reps)
            ProgramExecUiAction.SaveProgress -> saveProgress()
            is ProgramExecUiAction.ChangeWeight -> changeWeight(index = action.index, weight = action.weight)
            ProgramExecUiAction.UpdateStoredProgress -> updateStoredProgress()
        }
    }
    
    private val _saveCompleted: MutableSharedFlow<Unit> = MutableSharedFlow()
        val saveCompleted = _saveCompleted.asSharedFlow()

    init {
        viewModelScope.launch {
            _uiState.update {
                useCase.getTrainingStateUseCase()
            }
        }
    }

    private fun changeRepsDone(reps: Float, index: Int) {
        _uiState.update {
            val updatedProgramProgress = it.programProgress?.mapIndexed { id, programProgress ->
                if (id == index) programProgress.copy(reps = reps.toInt())
                else programProgress
            }
            it.copy(programProgress = updatedProgramProgress)
        }
    }

    private fun changeWeight(weight: Float, index: Int) {
        _uiState.update {
            val updatedProgramProgress = it.programProgress?.mapIndexed { id, programProgress ->
                if (id == index) programProgress.copy(weight = weight)
                else programProgress
            }
            it.copy(programProgress = updatedProgramProgress)
        }
    }

    private fun saveProgress() {
        viewModelScope.launch {
            useCase.finishTrainingUseCase(_uiState.value.programProgress ?: emptyList())
            _saveCompleted.emit(Unit)
        }
    }

    private fun updateStoredProgress() {
        viewModelScope.launch {
            useCase.updateStoredProgressUseCase(_uiState.value.programProgress ?: emptyList())
        }
    }
}