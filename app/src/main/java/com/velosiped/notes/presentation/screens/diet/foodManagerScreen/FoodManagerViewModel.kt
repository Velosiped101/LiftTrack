package com.velosiped.notes.presentation.screens.diet.foodManagerScreen

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.data.database.food.Food
import com.velosiped.notes.domain.usecase.diet.DietUseCase
import com.velosiped.notes.utils.CameraHelper
import com.velosiped.notes.utils.toFood
import com.velosiped.notes.utils.toFoodInput
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
    private val useCase: DietUseCase,
    private val cameraHelper: CameraHelper
): ViewModel() {
    private val _uiState: MutableStateFlow<FoodManagerUiState> = MutableStateFlow(FoodManagerUiState())
        val uiState = _uiState.asStateFlow()

    private val _loadingFinished = MutableSharedFlow<Unit>()
        val loadingFinished = _loadingFinished.asSharedFlow()

    private val tempFileUri = cameraHelper.createTemporaryFile()

    init {
        _uiState.update {
            it.copy(tempFileUri = tempFileUri)
        }
        viewModelScope.launch {
            useCase.observeFoodListUseCase().collect { list ->
                _uiState.update { it.copy(foodList = list) }
                _loadingFinished.emit(Unit)
            }
        }
    }

    fun updateInputImageUri() {
        val updatedUri = uiState.value.tempFileUri
            ?.buildUpon()
            ?.appendQueryParameter("key", System.currentTimeMillis().toString())
            ?.build()
        val pickedFoodInput = uiState.value.pickedFoodInput.copy(imageUri = updatedUri)
        _uiState.update {
            it.copy(
                pickedFoodInput = pickedFoodInput
            )
        }
    }

    fun setInputImageUriToNull() {
        val pickedFoodInput = uiState.value.pickedFoodInput.copy(imageUri = null)
        _uiState.update {
            it.copy(
                pickedFoodInput = pickedFoodInput
            )
        }
    }

    fun deleteMarkedFood() {
        val list = uiState.value.selectedForDeleteList
        viewModelScope.launch {
            useCase.deleteFoodFromDbUseCase(list)
        }
        list.mapNotNull { it.imageUrl }.forEach {
            cameraHelper.deleteFile(it.toUri())
        }
        _uiState.update {
            it.copy(selectedForDeleteList = emptyList())
        }
        exitDeleteMode()
    }

    fun saveChanges() {
        val imageUri = uiState.value.pickedFoodInput.imageUri.let {
            if (it == null) null
            else cameraHelper.createFile(it)
        }
        val food = uiState.value.pickedFoodInput.copy(imageUri = imageUri)
        viewModelScope.launch {
            useCase.addFoodToDbUseCase(food.toFood())
        }
    }

    fun exitDeleteMode() {
        if (_uiState.value.inDeleteMode) {
            _uiState.update {
                it.copy(inDeleteMode = false, selectedForDeleteList = listOf())
            }
        }
    }

    fun setInitialFoodInputState(food: Food?) {
        _uiState.update {
            it.copy(
                pickedFood = food,
                pickedFoodInput = food.toFoodInput()
            )
        }
    }

    fun markFoodForDelete(food: Food) {
        val updatedList = if (uiState.value.selectedForDeleteList.isEmpty()) {
            listOf(food)
        } else {
            uiState.value.selectedForDeleteList.toMutableList().apply {
                add(food)
            }.toList()
        }
        _uiState.update {
            it.copy(
                selectedForDeleteList = updatedList,
                inDeleteMode = true
            )
        }
    }

    fun onFoodNameChanged(value: String) {
        _uiState.update {
            it.copy(pickedFoodInput = it.pickedFoodInput.copy(name = value))
        }
    }

    fun onProteinChanged(value: String) {
        _uiState.update {
            it.copy(pickedFoodInput = it.pickedFoodInput.copy(protein = value))
        }
    }

    fun onFatChanged(value: String) {
        _uiState.update {
            it.copy(pickedFoodInput = it.pickedFoodInput.copy(fat = value))
        }
    }

    fun onCarbsChanged(value: String) {
        _uiState.update {
            it.copy(pickedFoodInput = it.pickedFoodInput.copy(carbs = value))
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraHelper.deleteFile(tempFileUri)
    }
}