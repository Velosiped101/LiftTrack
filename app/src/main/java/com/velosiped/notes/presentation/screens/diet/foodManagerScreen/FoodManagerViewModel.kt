package com.velosiped.notes.presentation.screens.diet.foodManagerScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.usecase.diet.DietUseCase
import com.velosiped.notes.utils.Constants
import com.velosiped.notes.utils.Nutrient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodManagerViewModel @Inject constructor(
    private val useCase: DietUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<FoodManagerUiState> = MutableStateFlow(FoodManagerUiState())
        val uiState = _uiState.asStateFlow()
        fun uiActions(action: FoodManagerUiAction) {
            when (action) {
                is FoodManagerUiAction.DeleteFood -> deleteFood(action.context)
                FoodManagerUiAction.ExitDeleteMode -> exitDeleteMode()
                is FoodManagerUiAction.GetFoodInformation -> getFoodInformation(action.food)
                FoodManagerUiAction.OnConfirmDialog -> confirmDialog()
                is FoodManagerUiAction.OnFoodClick -> onFoodClick(action.food)
                is FoodManagerUiAction.OnFoodLongClick -> onFoodLongClick(action.food)
                is FoodManagerUiAction.OnFoodCarbsChanged -> onFoodNutrientChanged(nutrient = Nutrient.Carbs, action.carbs)
                is FoodManagerUiAction.OnFoodFatChanged -> onFoodNutrientChanged(nutrient = Nutrient.Fat, action.fat)
                is FoodManagerUiAction.OnFoodNameChanged -> onFoodNameChanged(action.name)
                is FoodManagerUiAction.OnFoodProteinChanged -> onFoodNutrientChanged(nutrient = Nutrient.Protein, action.protein)
                is FoodManagerUiAction.CreateImageFile -> createImageFile(action.context)
                is FoodManagerUiAction.DeleteImageFile -> deleteImageFile(context = action.context, uri = action.uri)
                is FoodManagerUiAction.UpdateImageFile -> updateImageFile(action.context)
                FoodManagerUiAction.DeleteFoodPicture -> deleteFoodPicture()
            }
        }

    private val _loadingFinished = MutableSharedFlow<Unit>()
    val loadingFinished = _loadingFinished.asSharedFlow()

    init {
        viewModelScope.launch {
            useCase.observeFoodListUseCase().collect { list ->
                _uiState.update { it.copy(foodList = list) }
                _loadingFinished.emit(Unit)
            }
        }
    }

    private fun exitDeleteMode() {
        if (_uiState.value.isInDeleteMode) {
            _uiState.update {
                it.copy(isInDeleteMode = false, selectedForDeleteList = listOf())
            }
        }
    }

    private fun getFoodInformation(food: Food?) {
        _uiState.update {
            val pickedFoodInput = useCase.getFoodInformationUseCase(food)
            it.copy(
                pickedFood = food,
                pickedFoodInput = pickedFoodInput
            )
        }
    }

    private fun deleteFood(context: Context) {
        viewModelScope.launch {
            useCase.deleteFoodFromDbUseCase(_uiState.value.selectedForDeleteList, context)
        }
        _uiState.update {
            it.copy(
                selectedForDeleteList = listOf(),
                isInDeleteMode = false
            )
        }
    }

    private fun onFoodClick(food: Food) {
        if (_uiState.value.isInDeleteMode) {
            val updatedList = useCase.foodClickedInDeleteModeUseCase(food, _uiState.value.selectedForDeleteList)
            _uiState.update {
                it.copy(
                    selectedForDeleteList = updatedList,
                    isInDeleteMode = updatedList.isNotEmpty()
                )
            }
        } else {
            getFoodInformation(food)
        }
    }

    private fun onFoodLongClick(food: Food) {
        if (!uiState.value.isInDeleteMode) {
            _uiState.update {
                it.copy(
                    selectedForDeleteList = listOf(food),
                    isInDeleteMode = true
                )
            }
        }
    }

    private fun confirmDialog() {
        val pickedFood = _uiState.value.pickedFood
        val pickedFoodInput = _uiState.value.pickedFoodInput
       // if (!useCase.validateFoodInputUseCase(pickedFoodInput)) return
        viewModelScope.launch { useCase.addFoodToDbUseCase(pickedFood, pickedFoodInput) }
    }

    private fun onFoodNameChanged(name: String) {
        _uiState.update {
            it.copy(pickedFoodInput = it.pickedFoodInput.copy(name = name))
        }
    }

    private fun onFoodNutrientChanged(nutrient: Nutrient, input: String) {
        if (input.matches(Regex(Constants.DOUBLE_PATTERN))) {
            _uiState.update {
                when (nutrient) {
                    Nutrient.Protein -> it.copy(pickedFoodInput = it.pickedFoodInput.copy(protein = input))
                    Nutrient.Fat -> it.copy(pickedFoodInput = it.pickedFoodInput.copy(fat = input))
                    Nutrient.Carbs -> it.copy(pickedFoodInput = it.pickedFoodInput.copy(carbs = input))
                }
            }
        }
    }

    private fun deleteFoodPicture() {
        val updatedFoodInput = _uiState.value.pickedFoodInput.copy(imageUri = null)
        _uiState.update {
            it.copy(pickedFoodInput = updatedFoodInput)
        }
    }

    private fun updateImageFile(context: Context) {
        deleteImageFile(context, _uiState.value.pickedFood?.imageUrl)
        _uiState.update {
            val uri = it.generatedUri
                ?.buildUpon()
                ?.appendQueryParameter("timestamp", System.currentTimeMillis().toString())
                ?.build()
                .toString()
            it.copy(pickedFoodInput = it.pickedFoodInput.copy(imageUri = uri))
        }
    }

    private fun createImageFile(context: Context) {
        val imageUri = useCase.createImageFileUseCase(context)
        _uiState.update {
            it.copy(generatedUri = imageUri)
        }
    }

    private fun deleteImageFile(context: Context, uri: String?) {
        useCase.deleteImageFileUseCase(uri, context)
    }
}