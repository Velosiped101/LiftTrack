package com.velosiped.foodmanager.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.diet.food.repository.Food
import com.velosiped.diet.food.repository.FoodRepository
import com.velosiped.foodmanager.presentation.utils.FoodInputState
import com.velosiped.foodmanager.presentation.utils.toFood
import com.velosiped.foodmanager.presentation.utils.toFoodInputState
import com.velosiped.ui.model.textfieldvalidator.TextFieldValidator
import com.velosiped.utility.camerahelper.CameraHelper
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
    private val foodRepository: FoodRepository,
    private val cameraHelper: CameraHelper,
    private val textFieldValidator: TextFieldValidator
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
            foodRepository.getAllDatabaseItems().collect { list ->
                _uiState.update { it.copy(foodList = list) }
                _loadingFinished.emit(Unit)
            }
        }
    }

    fun onPhotoTaken() {
        val updatedUri = uiState.value.tempFileUri
            ?.buildUpon()
            ?.appendQueryParameter("key", System.currentTimeMillis().toString())
            ?.build()
        val updatedFoodInputState = uiState.value.foodInputState.copy(imageUri = updatedUri)
        _uiState.update {
            it.copy(foodInputState = updatedFoodInputState)
        }
    }

    fun onDeleteCurrentPhoto() {
        val updatedFoodInputState = uiState.value.foodInputState.copy(imageUri = null)
        _uiState.update {
            it.copy(foodInputState = updatedFoodInputState)
        }
    }

    fun onDeleteMarkedFood() {
        val list = uiState.value.markedForDeleteList
        viewModelScope.launch {
            foodRepository.deleteFromDatabase(list)
        }
        list.mapNotNull { it.imageUri }.forEach {
            cameraHelper.deleteFile(it)
        }
        onExitDeleteMode()
    }

    fun onSaveChanges() {
        val imageUri = uiState.value.foodInputState.imageUri.let {
            if (it == null) null
            else cameraHelper.createFile(it)
        }
        val foodState = uiState.value.foodInputState.copy(imageUri = imageUri)
        viewModelScope.launch {
            foodRepository.insertToDatabase(foodState.toFood())
        }
    }

    fun onExitDeleteMode() {
        _uiState.update { it.copy(markedForDeleteList = emptyList()) }
    }

    fun onFoodClick(food: Food?) {
        if (uiState.value.markedForDeleteList.isEmpty()) {
            _uiState.update {
                it.copy(foodInputState = food?.toFoodInputState() ?: FoodInputState())
            }
        } else {
            food?.let { markFoodForDelete(it) }
        }
    }

    fun onFoodLongClick(food: Food) {
        if (uiState.value.markedForDeleteList.isEmpty()) {
            markFoodForDelete(food)
        }
    }

    fun onNameChange(input: String) {
        val updatedNameFieldState = uiState.value.foodInputState.nameFieldState.copy(text = input)
        _uiState.update {
            it.copy(foodInputState = it.foodInputState.copy(nameFieldState = updatedNameFieldState))
        }
    }

    fun onProteinChange(value: String) {
        val inputIsValid = textFieldValidator.validateNutrient(value)
        if (inputIsValid) {
            val updatedProteinFieldState = uiState.value.foodInputState.proteinFieldState.copy(text = value)
            _uiState.update {
                it.copy(foodInputState = it.foodInputState.copy(proteinFieldState = updatedProteinFieldState))
            }
        }
    }

    fun onFatChange(value: String) {
        val inputIsValid = textFieldValidator.validateNutrient(value)
        if (inputIsValid) {
            val updatedFatFieldState = uiState.value.foodInputState.fatFieldState.copy(text = value)
            _uiState.update {
                it.copy(foodInputState = it.foodInputState.copy(fatFieldState = updatedFatFieldState))
            }
        }
    }

    fun onCarbsChange(value: String) {
        val inputIsValid = textFieldValidator.validateNutrient(value)
        if (inputIsValid) {
            val updatedCarbsFieldState = uiState.value.foodInputState.carbsFieldState.copy(text = value)
            _uiState.update {
                it.copy(foodInputState = it.foodInputState.copy(carbsFieldState = updatedCarbsFieldState))
            }
        }
    }

    fun markFoodForDelete(food: Food) {
        val updatedList = uiState.value.markedForDeleteList.toMutableList().apply {
            if (this.contains(food)) remove(food)
            else add(food)
        }.toList()
        _uiState.update {
            it.copy(markedForDeleteList = updatedList)
        }
    }

    override fun onCleared() {
        super.onCleared()
        cameraHelper.deleteFile(tempFileUri)
    }
}