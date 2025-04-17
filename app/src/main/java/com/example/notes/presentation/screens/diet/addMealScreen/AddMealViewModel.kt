package com.example.notes.presentation.screens.diet.addMealScreen

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.DietRepository
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.saveddata.mealhistory.MealHistory
import com.example.notes.utils.EMPTY_STRING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class AddMealViewModel(
    private val repository: DietRepository = Service.dietRepository
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
                AddMealUiAction.ResetUi -> resetUiState()
                is AddMealUiAction.Search -> search(action.name)
                AddMealUiAction.SearchInLocal -> setSearchToLocal()
                AddMealUiAction.SearchInRemote -> setSearchToRemote()
                is AddMealUiAction.OnMassInputChanged -> onMassInputChanged(action.mass)
            }
        }

    private fun resetUiState() {
        _uiState.update {
            searchJob?.cancel()
            it.copy(
                holder = FoodHolder.Start(),
                searchBarText = EMPTY_STRING,
                foodMass = null,
                pickedFood = null,
                pickedFoodList = mutableStateMapOf()
            )
        }
    }

    private fun search(text: String) {
        if (text.isBlank()) {
            searchJob?.cancel()
            _uiState.update {
                it.copy(
                    searchBarText = text,
                    holder = FoodHolder.Start()
                )
            }
        } else {
            searchJob?.cancel()
            _uiState.update {
                it.copy(
                    searchBarText = text,
                    holder = FoodHolder.Loading()
                )
            }
            searchJob = viewModelScope.launch(Dispatchers.IO) {
                delay(1000L)
                val holder = repository.getCombinedFoodList(
                    text = _uiState.value.searchBarText,
                    getFromLocal = _uiState.value.getFromLocal,
                    getFromRemote = _uiState.value.getFromRemote
                )
                _uiState.update {
                    it.copy(
                        holder = holder
                    )
                }
            }
        }
    }

    private fun confirmMealAddition() {
        val time = Calendar.getInstance()
        val hour = time.get(Calendar.HOUR_OF_DAY)
        val minute = time.get(Calendar.MINUTE)
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

    private fun onMassInputChanged(massInput: String) {
        _uiState.update {
            val mass = massInput.toIntOrNull() ?: return
            it.copy(
                foodMass = mass
            )
        }
    }

    private fun addFoodToPickedList() {
        _uiState.update {
            val updatedList = it.pickedFoodList.apply {
                put(it.pickedFood!!, it.foodMass ?: 0)
            }
            it.copy(
                pickedFoodList = updatedList,
                searchBarText = "",
                holder = FoodHolder.Start()
            )
        }
    }

    private fun removeFoodFromPickedList(food: Food) {
        val updatedList = _uiState.value.pickedFoodList.apply {
            remove(food)
        }
        _uiState.update {
            it.copy(pickedFoodList = updatedList)
        }
    }

    private fun setSearchToLocal() {
        _uiState.update { it.copy(getFromLocal = !it.getFromLocal) }
    }

    private fun setSearchToRemote() {
        _uiState.update { it.copy(getFromRemote = !it.getFromRemote) }
    }
}