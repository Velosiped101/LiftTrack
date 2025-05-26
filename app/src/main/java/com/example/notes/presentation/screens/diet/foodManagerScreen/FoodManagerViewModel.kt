package com.example.notes.presentation.screens.diet.foodManagerScreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.toMutableStateList
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.repository.diet.DietRepository
import com.example.notes.data.database.food.Food
import com.example.notes.utils.Nutrient
import com.example.notes.utils.DOUBLE_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FoodManagerViewModel @Inject constructor(
    private val repository: DietRepository
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
            repository.getAll().collect { list ->
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
            val pickedFoodInput = food?.let {
                FoodInput(
                    name = food.name,
                    protein = food.protein.toString(),
                    fat = food.fat.toString(),
                    carbs = food.carbs.toString(),
                    imageUri = food.imageUrl
                )
            } ?: FoodInput()
            it.copy(
                pickedFood = food,
                pickedFoodInput = pickedFoodInput
            )
        }
    }

    private fun deleteFood(context: Context) {
        val imageUris = mutableListOf<Uri>()
        _uiState.value.selectedForDeleteList.filter { it.imageUrl != null }.forEach {
            val uri = it.imageUrl!!.toUri()
            imageUris.add(uri)
        }
        viewModelScope.launch {
            repository.delete(_uiState.value.selectedForDeleteList)
            imageUris.forEach {
                deleteImageFile(context = context, uri = it)
            }
        }
        _uiState.update {
            it.copy(
                selectedForDeleteList = listOf(),
                isInDeleteMode = false
            )
        }
    }

    private fun onFoodClick(food: Food) {
        val state = uiState.value
        if (state.isInDeleteMode) {
            val updatedList = state.selectedForDeleteList.toMutableStateList()
            if (updatedList.contains(food)) {
                updatedList.remove(food)
                _uiState.update {
                    it.copy(
                        selectedForDeleteList = updatedList,
                        isInDeleteMode = updatedList.isNotEmpty()
                    )
                }
            } else {
                updatedList.add(food)
                _uiState.update {
                    it.copy(selectedForDeleteList = updatedList)
                }
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
        if (_uiState.value.isFoodInputValid) viewModelScope.launch {
            repository.insert(
                pickedFood?.copy(
                    name = pickedFoodInput.name,
                    protein = pickedFoodInput.protein.toDouble(),
                    fat = pickedFoodInput.fat.toDouble(),
                    carbs = pickedFoodInput.carbs.toDouble(),
                    imageUrl = pickedFoodInput.imageUri
                ) ?: pickedFoodInput.toFood()
            )
        }
    }

    private fun onFoodNameChanged(name: String) {
        _uiState.update {
            it.copy(pickedFoodInput = it.pickedFoodInput.copy(name = name))
        }
    }

    private fun onFoodNutrientChanged(nutrient: Nutrient, input: String) {
        if (input.matches(Regex(DOUBLE_PATTERN))) {
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
        viewModelScope.launch { _uiState.value.pickedFood?.imageUrl?.toUri()
            ?.let { deleteImageFile(context = context, uri = it) } }
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
        val imageDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NotesImages")
        imageDir.mkdir()
        val image = File(imageDir, "img_${System.currentTimeMillis()}.jpg")
        val imageUri = FileProvider.getUriForFile(context,"${context.packageName}.fileProvider",image)
        _uiState.update {
            it.copy(generatedUri = imageUri)
        }
    }

    private fun deleteImageFile(context: Context, uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }
}