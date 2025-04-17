package com.example.notes.presentation.screens.diet.foodManagerScreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.compose.runtime.toMutableStateList
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.DietRepository
import com.example.notes.data.local.food.Food
import com.example.notes.utils.Nutrient
import com.example.notes.utils.DOUBLE_PATTERN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class FoodManagerViewModel(
    private val repository: DietRepository = Service.dietRepository
): ViewModel() {
    private val foodList = repository.getAll()
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
                FoodManagerUiAction.DeleteFoodImage -> deleteFoodImage()
                is FoodManagerUiAction.CreateImageFile -> createImageFile(action.context)
                is FoodManagerUiAction.DeleteImageFile -> deleteImageFile(context = action.context, uri = action.uri)
                FoodManagerUiAction.UpdateImage -> updateImage()
            }
        }

    init {
        viewModelScope.launch {
            foodList.collect { list ->
                _uiState.update { it.copy(foodList = list.toMutableStateList()) }
            }
        }
    }

    private fun exitDeleteMode() {
        if (_uiState.value.isInDeleteMode) {
            _uiState.update {
                it.copy(isInDeleteMode = false, selectedForDeleteList = mutableListOf())
            }
        }
    }

    private fun getFoodInformation(food: Food?) {
        if (food == null) {
            _uiState.update {
                it.copy(
                    pickedFood = FoodInput()
                )
            }
        } else {
            _uiState.update {
                val pickedFood = FoodInput(
                    name = food.name,
                    protein = food.protein.toString(),
                    fat = food.fat.toString(),
                    carbs = food.carbs.toString(),
                    imageUri = food.imageUrl
                )
                it.copy(pickedFood = pickedFood)
            }
        }
    }

    private fun deleteFood(context: Context) {
        val imageUris = mutableListOf<Uri>()
        _uiState.value.selectedForDeleteList.filter { it.imageUrl != null }.forEach {
            val uri = it.imageUrl!!.toUri()
            imageUris.add(uri)
        }
        viewModelScope.launch{
            repository.delete(_uiState.value.selectedForDeleteList)
            imageUris.forEach {
                deleteImageFile(context = context, uri = it)
            }
        }
        _uiState.update {
            it.copy(
                selectedForDeleteList = mutableListOf(),
                isInDeleteMode = false
            )
        }
    }

    private fun onFoodClick(food: Food) {
        uiState.value.apply {
            when (isInDeleteMode) {
                true -> {
                    when (selectedForDeleteList.contains(food)) {
                        true -> {
                            if (selectedForDeleteList.size == 1) {
                                _uiState.update {
                                    it.copy(
                                        selectedForDeleteList = mutableListOf(),
                                        isInDeleteMode = false
                                    )
                                }
                            } else {
                                val updatedList = selectedForDeleteList.apply { remove(food) }
                                _uiState.update {
                                    it.copy(
                                        selectedForDeleteList = updatedList
                                    )
                                }
                            }
                        }
                        false -> {
                            val updatedList = selectedForDeleteList.apply { add(food) }
                            _uiState.update {
                                it.copy(
                                    selectedForDeleteList = updatedList
                                )
                            }
                        }
                    }
                }
                false -> {
                    getFoodInformation(food)
                }
            }
        }
    }

    private fun onFoodLongClick(food: Food) {
        if (!uiState.value.isInDeleteMode) {
            _uiState.update {
                it.copy(
                    selectedForDeleteList = mutableListOf(food),
                    isInDeleteMode = true
                )
            }
        }
    }

    private fun confirmDialog() {
        val foodInput = _uiState.value.pickedFood
        if (_uiState.value.isFoodInputValid) viewModelScope.launch {
            repository.insert(
                Food(
                    name = foodInput.name,
                    protein = foodInput.protein.toDouble(),
                    fat = foodInput.fat.toDouble(),
                    carbs = foodInput.carbs.toDouble(),
                    imageUrl = foodInput.imageUri
                )
            )
        }
    }

    private fun deleteFoodImage() {
        _uiState.update {
            it.copy(
                pickedFood = it.pickedFood.copy(imageUri = null)
            )
        }
    }

    private fun onFoodNameChanged(name: String) {
        _uiState.update {
            it.copy(pickedFood = it.pickedFood.copy(name = name))
        }
    }

    private fun onFoodNutrientChanged(nutrient: Nutrient, input: String) {
        if (input.matches(Regex(DOUBLE_PATTERN))) {
            _uiState.update {
                when (nutrient) {
                    Nutrient.Protein -> it.copy(pickedFood = it.pickedFood.copy(protein = input))
                    Nutrient.Fat -> it.copy(pickedFood = it.pickedFood.copy(fat = input))
                    Nutrient.Carbs -> it.copy(pickedFood = it.pickedFood.copy(carbs = input))
                }
            }
        }
    }

    private fun updateImage() {
        _uiState.update {
            val uri = it.generatedUri.toString()
            it.copy(pickedFood = it.pickedFood.copy(imageUri = uri))
        }
    }

    private fun createImageFile(context: Context) {
        val imageDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NotesImages")
        imageDir.mkdir()
        val image = File(imageDir, "img_${System.currentTimeMillis()}.jpg")
        val imageUri = FileProvider.getUriForFile(context,"${context.packageName}.fileprovider",image)
        _uiState.update {
            it.copy(generatedUri = imageUri)
        }
    }

    private fun deleteImageFile(context: Context, uri: Uri) {
        context.contentResolver.delete(uri, null, null)
    }
}