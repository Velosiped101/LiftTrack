package com.example.notes.presentation.screens.mainScreen

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.DietRepository
import com.example.notes.data.ExerciseRepository
import com.example.notes.data.ProgramRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val dietRepository: DietRepository = Service.dietRepository,
    private val programRepository: ProgramRepository = Service.programRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<MainScreenUiState> = MutableStateFlow(MainScreenUiState())
        val uiState = _uiState.asStateFlow()
    private val mealHistory = dietRepository.getMealHistory()
    fun uiAction(action: MainScreenUiAction) {

    }

    init {
        viewModelScope.launch {
            val isRestDay = programRepository.getProgramForToday().isEmpty()
            mealHistory.collect { mealHistory ->
                _uiState.update {
                    it.copy(
                        mealHistory = mealHistory.toMutableStateList(),
                        isRestDay = isRestDay
                    )
                }
            }
        }
    }


    
}