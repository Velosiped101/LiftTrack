package com.velosiped.notes.presentation.screens.diet.addmeal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.TextFieldValidator
import com.velosiped.notes.domain.usecase.diet.DietUseCase
import com.velosiped.notes.utils.EMPTY
import com.velosiped.notes.utils.SearchMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val useCase: DietUseCase,
    private val textFieldValidator: TextFieldValidator
): ViewModel() {
    private var searchJob: Job? = null

    private val _uiState: MutableStateFlow<AddMealUiState> = MutableStateFlow(AddMealUiState())
        val uiState = _uiState.asStateFlow()

    private val _saveCompleted = MutableSharedFlow<Unit>()
        val saveCompleted = _saveCompleted.asSharedFlow()

    init {
        viewModelScope.launch {
            useCase.observeTotalNutrientsUseCase().collect { (targetCalories, nutrientsIntake) ->
                _uiState.update { state ->
                    state.copy(
                        currentIntake = nutrientsIntake,
                        targetCalories = targetCalories
                    )
                }
            }
        }
    }

    fun searchBarTextChanged(text: String) {
        _uiState.update {
            it.copy(
                pagingDataFlow = null,
                searchBarQuery = text
            )
        }
        if (text.isBlank()) {
            searchJob?.cancel()
        } else {
            startSearch()
        }
    }

    fun startSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            val pagingDataFlow = useCase.searchFoodUseCase(
                    name = uiState.value.searchBarQuery,
                    searchMode = uiState.value.searchMode
                ).cachedIn(viewModelScope)

            _uiState.update { it.copy(pagingDataFlow = pagingDataFlow) }
        }
    }

    fun confirmMealAddition() {
        viewModelScope.launch {
            useCase.confirmMealAdditionUseCase(uiState.value.selectedFoodMap)
            _saveCompleted.emit(Unit)
        }
    }

    fun onMassInputChanged(input: String) {
        val massIsValid = textFieldValidator.validateMass(input)
        if (massIsValid) _uiState.update {
            it.copy(selectedFoodMass = input)
        }
    }

    fun onFoodPicked(food: Food) {
        val pickedFood = useCase.managePickedFoodListUseCase.getFood(
            food = food,
            initialMap = uiState.value.selectedFoodMap
        )
        _uiState.update {
            it.copy(
                selectedFood = pickedFood.first,
                selectedFoodMass = pickedFood.second?.toString() ?: String.EMPTY
            )
        }
    }

    fun addFoodToPickedList() {
        val updatedList = useCase.managePickedFoodListUseCase.addFood(
            uiState.value.selectedFood!!,
            uiState.value.selectedFoodMass.toInt(),
            uiState.value.selectedFoodMap
        )
        _uiState.update {
            it.copy(
                selectedFoodMap = updatedList,
                searchBarQuery = String.EMPTY
            )
        }
    }

    fun removeFoodFromPickedList(food: Food) {
        val updatedList = useCase.managePickedFoodListUseCase.deleteFood(
            food = food,
            initialMap = uiState.value.selectedFoodMap
        )
        _uiState.update {
            it.copy(selectedFoodMap = updatedList)
        }
    }

    fun setSearchMode(searchMode: SearchMode) {
        if (searchMode != uiState.value.searchMode) {
            _uiState.update { it.copy(searchMode = searchMode) }
            if (uiState.value.searchBarQuery.isNotBlank()) startSearch()
        }
    }

    fun clearSearchBarInput() {
        _uiState.update { it.copy(searchBarQuery = String.EMPTY) }
    }
}