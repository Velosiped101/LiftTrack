package com.example.notes.presentation.screens.diet.newRecipeScreen

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.data.repository.diet.DietRepository
import com.example.notes.data.database.food.Food
import com.example.notes.data.database.ingredient.Ingredient
import com.example.notes.utils.Nutrient
import com.example.notes.utils.DOUBLE_PATTERN
import com.example.notes.utils.EMPTY_STRING
import com.example.notes.utils.GENERATED_ID_INITIAL
import com.example.notes.utils.INT_PATTERN
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.internal.filterList
import java.io.File
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class NewRecipeViewModel @Inject constructor(
    private val repository: DietRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<NewRecipeUiState> = MutableStateFlow(NewRecipeUiState())
        val uiState = _uiState.asStateFlow()
        fun uiActions(action: NewRecipeUiAction) {
            when (action) {
                NewRecipeUiAction.ConfirmFoodOptions -> {}
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
    private val ingredientsFlow = MutableStateFlow(EMPTY_STRING)

    private val _saveCompleted = MutableSharedFlow<Unit>()
    val saveCompleted = _saveCompleted.asSharedFlow()

    private fun confirmNewFood() {
        viewModelScope.launch {
            val totalMass = _uiState.value.let {
                if (it.useAutoMass) it.autoCalculatedTotalMass
                else it.userDefinedTotalMass.toIntOrNull() ?: return@launch
            }
            if (totalMass == 0) return@launch
            val ingredientsList = _uiState.value.ingredientsList
            repository.insert(
                Food(
                    name = _uiState.value.recipeName,
                    protein = ingredientsList.sumOf { it.protein.toDouble() * it.mass.toInt() / 100.0 } / (totalMass / 100.0),
                    fat = ingredientsList.sumOf { it.fat.toDouble() * it.mass.toInt() / 100.0 } / (totalMass / 100.0),
                    carbs = ingredientsList.sumOf { it.carbs.toDouble() * it.mass.toInt() / 100.0 } / (totalMass / 100.0),
                    imageUrl = _uiState.value.imageUri?.toString()
                )
            )
            ingredientsList.forEach {
                repository.insertIngredient(
                    Ingredient(
                        id = if (it.id < GENERATED_ID_INITIAL) it.id else null,
                        name = it.name,
                        protein = it.protein.toDouble(),
                        fat = it.fat.toDouble(),
                        carbs = it.carbs.toDouble()
                    )
                )
            }
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
                    if (name.length >= 3) repository.getIngredient(name)
                    else flowOf(emptyList())
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
        if (input.matches(Regex(DOUBLE_PATTERN))) {
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
        if (input.matches(Regex(INT_PATTERN)) && input.length < 5) { // TODO: change pattern to 4 symbols max
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
        if (input.matches(Regex(INT_PATTERN))) {
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
        val imageDir = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "NotesImages")
        imageDir.mkdir()
        val image = File(imageDir, "img_${System.currentTimeMillis()}.jpg")
        val imageUri = FileProvider.getUriForFile(context,"${context.packageName}.fileProvider",image)
        _uiState.update {
            it.copy(generatedUri = imageUri)
        }
    }

    private fun deleteImageFile(context: Context, uri: Uri) {
        viewModelScope.launch {
            context.applicationContext.contentResolver.delete(uri, null, null)
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