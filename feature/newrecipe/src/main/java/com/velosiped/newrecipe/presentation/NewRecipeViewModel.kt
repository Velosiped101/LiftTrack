package com.velosiped.newrecipe.presentation

import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.diet.food.repository.Food
import com.velosiped.diet.food.repository.FoodRepository
import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.diet.ingredient.repository.IngredientRepository
import com.velosiped.newrecipe.presentation.components.utils.IngredientInputState
import com.velosiped.newrecipe.utils.toIngredient
import com.velosiped.newrecipe.utils.toIngredientInputState
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.model.textfieldvalidator.TextFieldValidator
import com.velosiped.utility.camerahelper.CameraHelper
import com.velosiped.utility.extensions.EMPTY
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO
import com.velosiped.utility.extensions.cut
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    private val ingredientRepository: IngredientRepository,
    private val foodRepository: FoodRepository,
    private val textFieldValidator: TextFieldValidator,
    private val cameraHelper: CameraHelper
): ViewModel() {
    private val _uiState: MutableStateFlow<NewRecipeUiState> = MutableStateFlow(NewRecipeUiState())
        val uiState = _uiState.asStateFlow()

    private val ingredientsFlow = MutableStateFlow(String.EMPTY)

    private val _saveCompleteFlow = MutableSharedFlow<Unit>()
        val saveCompleteFlow = _saveCompleteFlow.asSharedFlow()

    private val tempFileUri = cameraHelper.createTemporaryFile()

    init {
        _uiState.update { it.copy(tempFileUri = tempFileUri) }
    }

    fun onPhotoTaken() {
        val updatedUri = uiState.value.tempFileUri
            ?.buildUpon()
            ?.appendQueryParameter("key", System.currentTimeMillis().toString())
            ?.build()
        _uiState.update {
            it.copy(currentImageUri = updatedUri)
        }
    }

    fun onDeleteCurrentPhoto() {
        _uiState.update { it.copy(currentImageUri = null) }
    }

    fun onConfirmNewFood() {
        viewModelScope.launch {
            val newRecipe = createNewFood()
            foodRepository.insertToDatabase(newRecipe)

            ingredientRepository.insertIngredients(
                uiState.value.ingredients.map { it.toIngredient() }
            )

            _saveCompleteFlow.emit(Unit)
        }
    }

    fun onIncreaseNumberOfIngredients() {
        if (_uiState.value.ingredients.size <= 20) _uiState.update {
            it.copy(ingredients = it.ingredients.toMutableList().apply { add(IngredientInputState()) })
        }
    }

    fun onDecreaseNumberOfIngredients() {
        if (_uiState.value.ingredients.size > 1) _uiState.update {
            it.copy(ingredients = it.ingredients.toMutableList().apply { removeAt(this.lastIndex) })
        }
    }

    fun onIngredientNameChange(ingredient: IngredientInputState, input: String) {
        viewModelScope.launch {
            _uiState.update {
                val updatedList = it.ingredients.map { item ->
                    if (ingredient == item) item.copy(
                        nameFieldState = TextFieldState(text = input)
                    )
                    else item
                }
                it.copy(ingredients = updatedList)
            }
            searchForIngredients(input)
        }
    }

    fun onIngredientProteinChange(ingredient: IngredientInputState, input: String) {
        val proteinIsValid = textFieldValidator.validateNutrient(input)
        if (proteinIsValid) {
            _uiState.update {
                val updatedList = it.ingredients.map { item ->
                    val updatedItem = item.copy(proteinFieldState = TextFieldState(text = input))
                    if (ingredient == item) updatedItem
                    else item
                }
                it.copy(ingredients = updatedList)
            }
        }
    }

    fun onIngredientFatChange(ingredient: IngredientInputState, input: String) {
        val fatIsValid = textFieldValidator.validateNutrient(input)
        if (fatIsValid) {
            _uiState.update {
                val updatedList = it.ingredients.map { item ->
                    val updatedItem = item.copy(fatFieldState = TextFieldState(text = input))
                    if (ingredient == item) updatedItem
                    else item
                }
                it.copy(ingredients = updatedList)
            }
        }
    }

    fun onIngredientCarbsChange(ingredient: IngredientInputState, input: String) {
        val carbsAreValid = textFieldValidator.validateNutrient(input)
        if (carbsAreValid) {
            _uiState.update {
                val updatedList = it.ingredients.map { item ->
                    val updatedItem = item.copy(carbsFieldState = TextFieldState(text = input))
                    if (ingredient == item) updatedItem
                    else item
                }
                it.copy(ingredients = updatedList)
            }
        }
    }

    fun onIngredientMassChange(ingredient: IngredientInputState, input: String) {
        val massIsValid = textFieldValidator.validateMass(input)
        if (massIsValid) {
            _uiState.update {
                val updatedList = it.ingredients.map { item ->
                    val updatedItem = item.copy(massFieldState = TextFieldState(text = input))
                    if (ingredient == item) updatedItem
                    else item
                }
                it.copy(ingredients = updatedList)
            }
        }
    }

    fun onRecipeNameChange(input: String) {
        _uiState.update {
            it.copy(recipeNameState = TextFieldState(text = input))
        }
    }

    fun onRecipeMassChange(input: String) {
        val massIsValid = textFieldValidator.validateMass(input)
        if (massIsValid) {
            _uiState.update {
                it.copy(recipeMassState = TextFieldState(text = input))
            }
        }
    }

    fun onDeleteIngredient(ingredient: IngredientInputState) {
        val ingredients = uiState.value.ingredients
        val sharedTempId = UUID.randomUUID()

        val defaultIngredient = IngredientInputState().copy(tempId = sharedTempId)
        val targetIngredient = ingredient.copy(tempId = sharedTempId)

        if (targetIngredient == defaultIngredient) {
            val updatedList = ingredients.toMutableList().apply {
                if (this.size > Int.ONE) remove(ingredient)
            }
            _uiState.update { it.copy(ingredients = updatedList) }
        } else {
            val updatedList = ingredients.map {
                if (it.tempId == ingredient.tempId) IngredientInputState() else it
            }
            _uiState.update { it.copy(ingredients = updatedList) }
        }
    }

    fun onAutoCompleteIngredientInput(
        ingredientInputState: IngredientInputState,
        ingredient: Ingredient
    ) {
        _uiState.update {
            val updatedList = it.ingredients.map { ingredientState ->
                if (ingredientState.tempId == ingredientInputState.tempId)
                    ingredient.toIngredientInputState()
                else ingredientState
            }
            it.copy(
                ingredients = updatedList
            )
        }
    }

    fun onMassSourceChange(useAutoMass: Boolean) {
        val mass = if (useAutoMass) uiState.value.autoCalculatedRecipeMass.toString() else String.EMPTY
        _uiState.update {
            it.copy(
                useAutoMass = useAutoMass,
                recipeMassState = it.recipeMassState.copy(text = mass)
            )
        }
    }

    fun onFocusChange(ingredientInputState: IngredientInputState, focusState: FocusState) {
        _uiState.update { it.copy(ingredientsFound = emptyList()) }
        if (focusState.isFocused) {
            searchForIngredients(ingredientInputState.nameFieldState.text)
        }
    }

    private fun searchForIngredients(input: String) {
        viewModelScope.launch {
            ingredientsFlow.value = input
            ingredientsFlow
                .debounce(INGREDIENT_REQUEST_DELAY)
                .distinctUntilChanged()
                .flatMapLatest { name ->
                    if (name.length >= INGREDIENT_SEARCH_QUERY_MIN_LENGTH)
                        ingredientRepository.getIngredients(name)
                    else flowOf(emptyList()
                    )
                }.collect { ingredients ->
                    val ingredientsFoundList = ingredients.mapIndexedNotNull { index, ingredient ->
                        if (index in (Int.ZERO..INGREDIENTS_FOUND_COUNT - Int.ONE)) ingredient else null
                    }
                    _uiState.update {
                        it.copy(ingredientsFound = ingredientsFoundList)
                    }
                }
        }
    }

    private fun createNewFood(): Food {
        val ingredients = uiState.value.ingredients
        val recipeName = uiState.value.recipeNameState.text

        val recipeMass = uiState.value.recipeMassState.text.toInt()

        val protein = (ingredients.sumOf {
            it.proteinFieldState.text.toFloat() * it.massFieldState.text.toInt() / NUTRITION_REFERENCE
        } / (recipeMass / NUTRITION_REFERENCE)).toFloat().cut()
        val fat = (ingredients.sumOf {
            it.fatFieldState.text.toFloat() * it.massFieldState.text.toInt() / NUTRITION_REFERENCE
        } / (recipeMass / NUTRITION_REFERENCE)).toFloat().cut()
        val carbs = (ingredients.sumOf {
            it.carbsFieldState.text.toFloat() * it.massFieldState.text.toInt() / NUTRITION_REFERENCE
        } / (recipeMass / NUTRITION_REFERENCE)).toFloat().cut()

        val imageUri = uiState.value.currentImageUri.let {
            if (it == null) null
            else cameraHelper.createFile(it)
        }

        return Food(
            id = null,
            name = recipeName,
            protein = protein,
            fat = fat,
            carbs = carbs,
            imageUri = imageUri
        )
    }

    override fun onCleared() {
        super.onCleared()
        cameraHelper.deleteFile(tempFileUri)
    }

    companion object {
        const val INGREDIENT_REQUEST_DELAY = 500L
        const val INGREDIENT_SEARCH_QUERY_MIN_LENGTH = 3
        const val NUTRITION_REFERENCE = 100.0
        const val INGREDIENTS_FOUND_COUNT = 5
    }
}