package com.velosiped.notes.presentation.screens.diet.addMealScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.data.database.saveddata.mealhistory.MealHistory
import com.velosiped.notes.data.repository.diet.DietRepository
import com.velosiped.notes.data.repository.tempProgress.AppProtoDataStoreRepository
import com.velosiped.notes.utils.EMPTY_STRING
import com.velosiped.notes.utils.INT_PATTERN
import com.velosiped.notes.utils.SearchMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val repository: DietRepository,
    private val protoDataStoreRepository: AppProtoDataStoreRepository
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
            combine(
                protoDataStoreRepository.appProtoStoreFlow,
                repository.getCurrentTotalNutrients()
            ) { dataStore, totalNutrients ->
                val targetCalories = dataStore.appPreferences.targetCalories
                Pair(targetCalories, totalNutrients)
            }.collect { (targetCalories, totalNutrients) ->
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
            val pagingDataFlow = repository.getFoodPage(
                name = _uiState.value.searchBarText,
                searchMode = _uiState.value.searchMode
            ).cachedIn(viewModelScope)
            _uiState.update { it.copy(pagingDataFlow = pagingDataFlow) }
        }
    }

    private fun confirmMealAddition() {
        val time = Calendar.getInstance()
        val hour = time.get(Calendar.HOUR_OF_DAY).let {
            if (it < 10) "0$it" else it.toString()
        }
        val minute = time.get(Calendar.MINUTE).let {
            if (it < 10) "0$it" else it.toString()
        }
        viewModelScope.launch {
            _uiState.value.pickedFoodList.entries.forEach {
                val meal = MealHistory(
                    time = "$hour : $minute",
                    name = it.key.name,
                    protein = it.key.protein,
                    fat = it.key.fat,
                    carbs = it.key.carbs,
                    mass = it.value,
                    totalCal = ((it.key.carbs+it.key.protein)*4 + it.key.fat*9)*it.value/100
                )
                repository.insertToHistory(meal)
            }
            _saveCompleted.emit(Unit)
        }
    }

    private fun onFoodPicked(food: Food) {
        _uiState.update {
            it.copy(
                pickedFood = food,
                foodMass =
                    if (it.pickedFoodList.containsKey(food)) it.pickedFoodList[food]
                    else null
            )
        }
    }

    private fun onMassInputChanged(input: String) {
        if (input.matches(Regex(INT_PATTERN)) && input.length < 5) {
            val mass = input.toIntOrNull()
            _uiState.update {
                it.copy(
                    foodMass = mass
                )
            }
        }
    }

    private fun addFoodToPickedList() {
        if (_uiState.value.isMassValid) _uiState.update {
            val updatedList = it.pickedFoodList.toMutableMap().apply {
                put(it.pickedFood!!, it.foodMass!!)
            }
            it.copy(
                pickedFoodList = updatedList,
                searchBarText = EMPTY_STRING
            )
        }
    }

    private fun removeFoodFromPickedList(food: Food) {
        val updatedList = _uiState.value.pickedFoodList.toMutableMap().apply {
            remove(food)
        }
        _uiState.update {
            it.copy(pickedFoodList = updatedList)
        }
    }

    private fun setSearchMode(searchMode: SearchMode) {
        if (searchMode != _uiState.value.searchMode) {
            _uiState.update {
                it.copy(
                    searchMode = searchMode
                )
            }
            if (_uiState.value.searchBarText.isNotBlank()) startSearch()
        }
    }

    private fun clearSearchBarInput() {
        _uiState.update {
            it.copy(searchBarText = EMPTY_STRING)
        }
    }
}