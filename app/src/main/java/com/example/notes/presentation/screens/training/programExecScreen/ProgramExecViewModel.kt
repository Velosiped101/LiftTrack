package com.example.notes.presentation.screens.training.programExecScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.database.saveddata.programProgress.ProgramProgress
import com.example.notes.data.repository.program.ProgramRepository
import com.example.notes.data.repository.tempProgress.AppProtoDataStoreRepository
import com.example.notes.data.repository.tempProgress.toProgramProgressList
import com.example.notes.data.repository.tempProgress.toProgramTempProgressItemsList
import com.example.notes.proto.AppProtoStore
import com.example.notes.utils.Date
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgramExecViewModel @Inject constructor(
    private val programRepository: ProgramRepository,
    private val appProtoDataStoreRepository: AppProtoDataStoreRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<ProgramExecUiState> = MutableStateFlow(ProgramExecUiState())
        val uiState = _uiState.asStateFlow()
    fun uiAction(action: ProgramExecUiAction) {
        when (action) {
            is ProgramExecUiAction.ChangeRepsDone -> changeRepsDone(index = action.index, reps = action.reps)
            ProgramExecUiAction.SaveProgress -> saveProgress()
            ProgramExecUiAction.StartNewTraining -> startNewTraining()
            is ProgramExecUiAction.ChangeWeight -> changeWeight(index = action.index, weight = action.weight)
            ProgramExecUiAction.UpdateStoredProgress -> updateStoredProgress()
        }
    }
    
    private val _saveCompleted: MutableSharedFlow<Unit> = MutableSharedFlow()
        val saveCompleted = _saveCompleted.asSharedFlow()

    private val appProtoStoreFlow = appProtoDataStoreRepository.appProtoStoreFlow
    private val programForToday = programRepository.getProgramForToday()

    init {
        viewModelScope.launch {
            appProtoStoreFlow.collect { savedData ->
                if (savedData.programExecLock.programExecutionLocked.not())
                    if (savedData.protoProgramProgressItemsList.isEmpty()) {
                        startNewTraining()
                    } else {
                        resumeTraining(savedData)
                    }
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
            _uiState.value.programProgress?.forEach {
                programRepository.insertToProgramProgress(it)
            }
            appProtoDataStoreRepository.finishProgramExecution()
            _saveCompleted.emit(Unit)
        }
    }

    private fun updateStoredProgress() {
        viewModelScope.launch {
            appProtoDataStoreRepository.saveTempProgramProgress(
                _uiState.value.programProgress?.toProgramTempProgressItemsList() ?: return@launch
            )
        }
    }

    private fun startNewTraining() {
        viewModelScope.launch {
            programForToday.collect { program ->
                val programState = program.map {
                    val weight = programRepository.getLatestWeightDone(it.exercise).first()
                    val showHint = programRepository.getWeightIncreaseHint(it.exercise).first()
                    Pair(
                        ProgramProgress(
                            day = Date.day,
                            month = Date.month,
                            year = Date.year,
                            reps = 0,
                            exercise = it.exercise,
                            repsPlanned = it.reps,
                            weight = weight
                        ), showHint
                    )
                }
                val programProgress = programState.map { it.first }
                val showHintList = programState.map { it.second }
                val initialWeight = programProgress.map { it.weight }
                appProtoDataStoreRepository.saveTempProgramProgress(programProgress.toProgramTempProgressItemsList())
                _uiState.update { state ->
                    state.copy(
                        programProgress = programProgress,
                        showHintList = showHintList,
                        initialWeight = initialWeight
                    )
                }
            }
        }
    }

    private fun resumeTraining(savedData: AppProtoStore) {
        viewModelScope.launch {
            val initialWeight = savedData.protoProgramProgressItemsList.map {
                programRepository.getLatestWeightDone(it.exercise).first()
            }
            val showHint = savedData.protoProgramProgressItemsList.map {
                programRepository.getWeightIncreaseHint(it.exercise).first()
            }
            _uiState.update {
                it.copy(
                    programProgress = savedData.protoProgramProgressItemsList.toProgramProgressList(),
                    showHintList = showHint,
                    initialWeight = initialWeight
                )
            }
        }
    }
}