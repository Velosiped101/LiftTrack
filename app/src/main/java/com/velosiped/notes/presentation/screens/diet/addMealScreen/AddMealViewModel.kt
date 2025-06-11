package com.velosiped.notes.presentation.screens.diet.addMealScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.usecase.diet.DietUseCase
import com.velosiped.notes.utils.Constants
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
    private val useCase: DietUseCase
): ViewModel() {
    private var searchJob: Job? = null

    private val _uiState: MutableStateFlow<AddMealUiState> = MutableStateFlow(AddMealUiState())
        val uiState = _uiState.asStateFlow()
        fun uiAction(action: AddMealUiAction) {
            when (action) {
                is AddMealUiAction.AddFoodToPickedList -> addFoodToPickedList()
                AddMealUiAction.ConfirmMeal -> confirmMealAddition()
                is AddMealUiAction.PickFood -> onFoodPicked(action.food)
                is AddMealUiAction.RemoveFromPickedList -> removeFoodFromPickedList(action.food)
                is AddMealUiAction.SearchBarTextChanged -> searchBarTextChanged(action.name)
                is AddMealUiAction.OnMassInputChanged -> onMassInputChanged(action.mass)
                is AddMealUiAction.SetSearchMode -> setSearchMode(action.searchMode)
                AddMealUiAction.ClearSearchBarInput -> clearSearchBarInput()
            }
        }

    private val _saveCompleted = MutableSharedFlow<Unit>()
    val saveCompleted = _saveCompleted.asSharedFlow()

    init {
        viewModelScope.launch {
            useCase.observeTotalNutrientsUseCase()
                .collect { (targetCalories, totalNutrients) ->
                    _uiState.update { state ->
                        state.copy(
                            currentTotalCals = totalNutrients.cals,
                            currentTotalProtein = totalNutrients.protein,
                            currentTotalFat = totalNutrients.fat,
                            currentTotalCarbs = totalNutrients.carbs,
                            targetCalories = targetCalories
                        )
                    }
                }
        }
    }

    private fun searchBarTextChanged(text: String) {
        _uiState.update {
            it.copy(
                pagingDataFlow = null,
                searchBarText = text
            )
        }
        if (text.isBlank()) {
            searchJob?.cancel()
        } else {
            startSearch()
        }
    }

    private fun startSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(1000L)
            val pagingDataFlow = useCase
                .searchFoodUseCase(_uiState.value.searchBarText, _uiState.value.searchMode)
                .cachedIn(viewModelScope)
            _uiState.update { it.copy(pagingDataFlow = pagingDataFlow) }
        }
    }

    private fun confirmMealAddition() {
        viewModelScope.launch {
            useCase.confirmMealAdditionUseCase(_uiState.value.pickedFoodList)
            _saveCompleted.emit(Unit)
        }
    }

    private fun onMassInputChanged(input: String) {
        val mass = useCase.validateMassUseCase(input)
        _uiState.update {
            it.copy(foodMass = mass)
        }
    }

    private fun onFoodPicked(food: Food) {
        val pickedFood = useCase.managePickedFoodListUseCase.getFood(food, _uiState.value.pickedFoodList)
        _uiState.update {
            it.copy(
                pickedFood = pickedFood.first,
                foodMass = pickedFood.second
            )
        }
    }

    private fun addFoodToPickedList() {
        val updatedList = useCase.managePickedFoodListUseCase.addFood(
            _uiState.value.pickedFood!!,
            _uiState.value.foodMass!!,
            _uiState.value.pickedFoodList
        )
        _uiState.update {
            it.copy(
                pickedFoodList = updatedList,
                searchBarText = Constants.EMPTY_STRING
            )
        }
    }

    private fun removeFoodFromPickedList(food: Food) {
        val updatedList = useCase.managePickedFoodListUseCase.deleteFood(food, _uiState.value.pickedFoodList)
        _uiState.update {
            it.copy(pickedFoodList = updatedList)
        }
    }

    private fun setSearchMode(searchMode: SearchMode) {
        if (searchMode != _uiState.value.searchMode) {
            _uiState.update { it.copy(searchMode = searchMode) }
            if (_uiState.value.searchBarText.isNotBlank()) startSearch()
        }
    }

    private fun clearSearchBarInput() {
        _uiState.update { it.copy(searchBarText = Constants.EMPTY_STRING) }
    }
}