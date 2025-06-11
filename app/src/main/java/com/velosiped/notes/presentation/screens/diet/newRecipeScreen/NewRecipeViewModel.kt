package com.velosiped.notes.presentation.screens.diet.newRecipeScreen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.data.database.ingredient.Ingredient
import com.velosiped.notes.domain.usecase.diet.DietUseCase
import com.velosiped.notes.utils.Constants
import com.velosiped.notes.utils.Nutrient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    private val useCase: DietUseCase
): ViewModel() {
    private val _uiState: MutableStateFlow<NewRecipeUiState> = MutableStateFlow(NewRecipeUiState())
        val uiState = _uiState.asStateFlow()
        fun uiActions(action: NewRecipeUiAction) {
            when (action) {
                NewRecipeUiAction.ConfirmNewFood -> confirmNewFood()
                NewRecipeUiAction.DecreaseNumberOfIngredients -> decreaseNumberOfIngredients()
                is NewRecipeUiAction.DeleteIngredient -> deleteIngredient(action.ingredient)
                NewRecipeUiAction.IncreaseNumberOfIngredients -> increaseNumberOfIngredients()
                is NewRecipeUiAction.OnIngredientCarbsChanged -> onIngredientNutrientChanged(ingredient = action.ingredient, input = action.input, nutrient = Nutrient.Carbs)
                is NewRecipeUiAction.OnIngredientFatChanged -> onIngredientNutrientChanged(ingredient = action.ingredient, input = action.input, nutrient = Nutrient.Fat)
                is NewRecipeUiAction.OnIngredientMassChanged -> onIngredientMassChanged(ingredient = action.ingredient, input = action.input)
                is NewRecipeUiAction.OnIngredientNameChanged -> onIngredientNameChanged(ingredient = action.ingredient, name = action.input)
                is NewRecipeUiAction.OnIngredientProteinChanged -> onIngredientNutrientChanged(ingredient = action.ingredient, input = action.input, nutrient = Nutrient.Protein)
                is NewRecipeUiAction.OnRecipeMassChanged -> onRecipeMassChanged(input = action.input)
                is NewRecipeUiAction.OnRecipeNameChanged -> onRecipeNameChanged(name = action.input)
                is NewRecipeUiAction.FillIngredientInfo -> fillIngredientInfo(ingredient = action.ingredient, ingredientInput = action.ingredientInput)
                is NewRecipeUiAction.CreateImageFile -> createImageFile(action.context)
                NewRecipeUiAction.DeleteFoodImage -> deleteFoodImage()
                is NewRecipeUiAction.DeleteImageFile -> deleteImageFile(action.context, action.uri)
                NewRecipeUiAction.UpdateImage -> updateImage()
                is NewRecipeUiAction.ChangeMassSource -> changeMassSource(action.useAutoMass)
            }
        }

    private val ingredientsFlow = MutableStateFlow(Constants.EMPTY_STRING)

    private val _saveCompleted = MutableSharedFlow<Unit>()
    val saveCompleted = _saveCompleted.asSharedFlow()

    private fun confirmNewFood() {
        viewModelScope.launch {
            useCase.createNewFoodUseCase(_uiState.value)
            useCase.createNewIngredientsUseCase(_uiState.value)
            _saveCompleted.emit(Unit)
        }
    }

    private fun increaseNumberOfIngredients() {
        val newId = _uiState.value.ingredientsList.maxOf { it.id } + 1
        val emptyIngredient = IngredientInput(id = newId)
        if (_uiState.value.ingredientsList.size <= 20) _uiState.update {
            it.copy(ingredientsList = it.ingredientsList.toMutableList().apply { add(emptyIngredient) })
        }
    }

    private fun decreaseNumberOfIngredients() {
        if (_uiState.value.ingredientsList.size > 1) _uiState.update {
            it.copy(ingredientsList = it.ingredientsList.toMutableList().apply { removeLast() })
        }
    }

    private fun onIngredientNameChanged(ingredient: IngredientInput, name: String) {
        viewModelScope.launch {
            _uiState.update {
                val updatedList = it.ingredientsList.map { item ->
                    if (ingredient == item) item.copy(name = name)
                    else item
                }
                it.copy(
                    ingredientsList = updatedList,
                    ingredientsFoundList = emptyList()
                )
            }
            ingredientsFlow.value = name
            ingredientsFlow
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { name ->
                    useCase.searchForIngredientsUseCase(name)
                }.collect { ingredients ->
                    val ingredientsFoundList = ingredients.mapIndexedNotNull { index, ingredient ->
                        if (index in (0..4)) ingredient else null
                    }
                    _uiState.update {
                        it.copy(ingredientsFoundList = ingredientsFoundList)
                    }
                }
        }
    }

    private fun onIngredientNutrientChanged(ingredient: IngredientInput, input: String, nutrient: Nutrient) {
        if (input.matches(Regex(Constants.DOUBLE_PATTERN))) {
            _uiState.update {
                val updatedList = it.ingredientsList.map { item ->
                    val updatedItem = when (nutrient) {
                        Nutrient.Protein -> item.copy(protein = input)
                        Nutrient.Fat -> item.copy(fat = input)
                        Nutrient.Carbs -> item.copy(carbs = input)
                    }
                    if (ingredient == item) updatedItem
                    else item
                }
                it.copy(ingredientsList = updatedList)
            }
        }
    }

    private fun onIngredientMassChanged(ingredient: IngredientInput, input: String) {
        if (input.matches(Regex(Constants.INT_PATTERN)) && input.length < 5) {
            _uiState.update {
                val updatedList = it.ingredientsList.map { item ->
                    if (ingredient == item) item.copy(mass = input)
                    else item
                }
                it.copy(
                    ingredientsList = updatedList
                )
            }
        }
    }

    private fun onRecipeNameChanged(name: String) {
        _uiState.update {
            it.copy(recipeName = name)
        }
    }

    private fun onRecipeMassChanged(input: String) {
        if (input.matches(Regex(Constants.INT_PATTERN))) {
            _uiState.update {
                it.copy(userDefinedTotalMass = input)
            }
        }
    }

    private fun deleteIngredient(ingredient: IngredientInput) {
        _uiState.update {
            val updatedList =
                if (ingredient.isEmptyIngredient && it.ingredientsList.size > 1) {
                    it.ingredientsList.toMutableList().apply { remove(ingredient) }
                }
                else it.ingredientsList.map { item ->
                    if (item == ingredient) IngredientInput(id = item.id) else item
                }
            it.copy(ingredientsList = updatedList)
        }
    }

    private fun fillIngredientInfo(ingredientInput: IngredientInput, ingredient: Ingredient) {
        _uiState.update {
            val updatedList = it.ingredientsList.map { item ->
                if (item == ingredientInput) item.copy(
                    id = ingredient.id!!,
                    name = ingredient.name,
                    protein = ingredient.protein.toString(),
                    fat = ingredient.fat.toString(),
                    carbs = ingredient.carbs.toString(),
                    readOnly = true
                ) else item
            }
            it.copy(
                ingredientsList = updatedList
            )
        }
    }

    private fun deleteFoodImage() {
        _uiState.update {
            it.copy(
                imageUri = null
            )
        }
    }

    private fun updateImage() {
        _uiState.update {
            val uri = it.generatedUri
                ?.buildUpon()
                ?.appendQueryParameter("timestamp", System.currentTimeMillis().toString())
                ?.build()
            it.copy(imageUri = uri)
        }
    }

    private fun createImageFile(context: Context) {
        val imageUri = useCase.createImageFileUseCase(context)
        _uiState.update {
            it.copy(generatedUri = imageUri)
        }
    }

    private fun deleteImageFile(context: Context, uri: String?) {
        viewModelScope.launch {
            useCase.deleteImageFileUseCase(uri, context)
            _uiState.update {
                it.copy(generatedUri = null)
            }
            _saveCompleted.emit(Unit)
        }
    }

    private fun changeMassSource(useAutoMass: Boolean) {
        _uiState.update {
            it.copy(useAutoMass = useAutoMass)
        }
    }
}