package com.velosiped.addmeal.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.velosiped.addmeal.presentation.utils.SearchMode
import com.velosiped.datastore.DataStoreRepository
import com.velosiped.diet.food.repository.FoodRepository
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.model.textfieldvalidator.TextFieldValidator
import com.velosiped.diet.food.repository.Food
import com.velosiped.diet.mealhistory.repository.MealHistory
import com.velosiped.diet.mealhistory.repository.MealHistoryRepository
import com.velosiped.utility.data.NutrientsIntake
import com.velosiped.utility.extensions.EMPTY
import com.velosiped.utility.extensions.HUNDRED
import com.velosiped.utility.extensions.THOUSAND
import com.velosiped.utility.extensions.cut
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.collections.toMutableMap

@HiltViewModel
class AddMealViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val mealHistoryRepository: MealHistoryRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val textFieldValidator: TextFieldValidator
): ViewModel() {
    private var searchJob: Job? = null

    private val _uiState: MutableStateFlow<AddMealUiState> = MutableStateFlow(AddMealUiState())
        val uiState = _uiState.asStateFlow()

    private val _saveCompleted = MutableSharedFlow<Unit>()
        val saveCompleted = _saveCompleted.asSharedFlow()

    init {
        viewModelScope.launch {
            val mealHistory = mealHistoryRepository.getMealHistory().first()
            val targetCalories = dataStoreRepository.appProtoStoreFlow.first()
                .appPreferences
                .targetCalories
            val currentIntake = NutrientsIntake(
                protein = mealHistory.sumOf { it.protein.toDouble() * it.mass / Int.HUNDRED }.toFloat().cut(),
                fat = mealHistory.sumOf { it.fat.toDouble() * it.mass / Int.HUNDRED }.toFloat().cut(),
                carbs = mealHistory.sumOf { it.carbs.toDouble() * it.mass / Int.HUNDRED }.toFloat().cut()
            )
            _uiState.update { state ->
                state.copy(
                    currentIntake = currentIntake,
                    targetCalories = targetCalories
                )
            }
        }
    }

    fun onSearchInputChange(text: String) {
        _uiState.update {
            it.copy(
                pagingDataFlow = emptyFlow(),
                searchBarState = TextFieldState(text = text)
            )
        }
        if (text.isBlank()) {
            searchJob?.cancel()
        } else {
            startSearch()
        }
    }

    fun onConfirmMeal() {
        viewModelScope.launch {
            val foodList = uiState.value.selectedFoodMap
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY).let {
                if (it < 10) "0$it" else it.toString()
            }
            val minute = calendar.get(Calendar.MINUTE).let {
                if (it < 10) "0$it" else it.toString()
            }
            val time = "$hour : $minute"
            val mealHistory = foodList.map {
                MealHistory(
                    time = time,
                    name = it.key.name,
                    protein = it.key.protein.toFloat(),
                    fat = it.key.fat.toFloat(),
                    carbs = it.key.carbs.toFloat(),
                    mass = it.value
                )
            }
            mealHistoryRepository.insertToHistory(mealHistory)
            _saveCompleted.emit(Unit)
        }
    }

    fun onMassInputChange(input: String) {
        val massIsValid = textFieldValidator.validateMass(input)
        if (massIsValid) _uiState.update {
            it.copy(massInputState = TextFieldState(text = input))
        }
    }

    fun onFoodPicked(food: Food) {
        val map = uiState.value.selectedFoodMap
        val massInput = if (map.containsKey(food)) map[food].toString() else String.EMPTY
        _uiState.update {
            it.copy(
                selectedFood = food,
                massInputState = TextFieldState(text = massInput)
            )
        }
    }

    fun onConfirmAddingToSelectedMap() {
        val selectedFood = uiState.value.selectedFood
        val mass = uiState.value.massInputState.text.toInt()
        val updatedMap = uiState.value.selectedFoodMap.toMutableMap().apply {
            if (this.containsKey(selectedFood)) remove(selectedFood)
            selectedFood?.let { put(it, mass) }
        }
        _uiState.update {
            it.copy(
                selectedFoodIntake = calculateSelectedFoodIntake(updatedMap),
                selectedFoodMap = updatedMap,
                searchBarState = TextFieldState()
            )
        }
    }

    fun onDeleteFromSelectedMap(food: Food) {
        val updatedMap = uiState.value.selectedFoodMap.toMutableMap().apply { remove(food) }
        _uiState.update {
            it.copy(
                selectedFoodIntake = calculateSelectedFoodIntake(updatedMap),
                selectedFoodMap = updatedMap
            )
        }
    }

    fun onSearchModeChange(searchMode: SearchMode) {
        if (searchMode != uiState.value.searchMode) {
            _uiState.update { it.copy(searchMode = searchMode) }
            if (uiState.value.searchBarState.text.isNotBlank()) startSearch()
        }
    }

    fun onSearchInputClear() {
        _uiState.update { it.copy(searchBarState = TextFieldState()) }
    }

    fun startSearch() {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(Float.THOUSAND.toLong())
            val searchQuery = uiState.value.searchBarState.text.trim()
            val searchInLocal = uiState.value.searchMode == SearchMode.Local

            if (searchQuery.isBlank()) emptyFlow<PagingData<Food>>()
            val pagingDataFlow = foodRepository
                .getFoodApiPage(searchQuery, searchInLocal)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
            _uiState.update { it.copy(pagingDataFlow = pagingDataFlow) }
        }
    }

    private fun calculateSelectedFoodIntake(map: Map<Food, Int>): NutrientsIntake = NutrientsIntake(
        protein = map.entries.sumOf { it.key.protein * it.value / Int.HUNDRED.toDouble() }.toFloat().cut(),
        fat = map.entries.sumOf { it.key.fat * it.value / Int.HUNDRED.toDouble() }.toFloat().cut(),
        carbs = map.entries.sumOf { it.key.carbs * it.value / Int.HUNDRED.toDouble() }.toFloat().cut()
    )
}