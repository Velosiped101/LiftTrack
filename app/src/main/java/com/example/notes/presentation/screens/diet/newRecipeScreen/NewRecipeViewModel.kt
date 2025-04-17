package com.example.notes.presentation.screens.diet.newRecipeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notes.Service
import com.example.notes.data.DietRepository
import com.example.notes.data.local.food.Food
import com.example.notes.data.local.food.Ingredient
import com.example.notes.utils.Nutrient
import com.example.notes.utils.DOUBLE_PATTERN
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NewRecipeViewModel(
    private val repository: DietRepository = Service.dietRepository
): ViewModel() {
    private val _uiState: MutableStateFlow<NewRecipeUiState> = MutableStateFlow(NewRecipeUiState())
        val uiState = _uiState.asStateFlow()
        fun uiActions(action: NewRecipeUiAction) {
            when (action) {
                NewRecipeUiAction.ConfirmFoodOptions -> {}
                NewRecipeUiAction.ConfirmNewFood -> confirmNewFood()
                NewRecipeUiAction.DecreaseNumberOfIngredients -> decreaseNumberOfIngredients()
                is NewRecipeUiAction.DeleteIngredient -> deleteIngredient(action.id)
                NewRecipeUiAction.IncreaseNumberOfIngredients -> increaseNumberOfIngredients()
                is NewRecipeUiAction.OnIngredientCarbsChanged -> onIngredientNutrientChanged(id = action.id, input = action.text, nutrient = Nutrient.Carbs)
                is NewRecipeUiAction.OnIngredientFatChanged -> onIngredientNutrientChanged(id = action.id, input = action.text, nutrient = Nutrient.Fat)
                is NewRecipeUiAction.OnIngredientMassChanged -> onIngredientMassChanged(id = action.id, text = action.text)
                is NewRecipeUiAction.OnIngredientNameChanged -> onIngredientNameChanged(id = action.id, name = action.text)
                is NewRecipeUiAction.OnIngredientProteinChanged -> onIngredientNutrientChanged(id = action.id, input = action.text, nutrient = Nutrient.Protein)
                is NewRecipeUiAction.OnRecipeMassChanged -> onRecipeMassChanged(text = action.text)
                is NewRecipeUiAction.OnRecipeNameChanged -> onRecipeNameChanged(name = action.text)
                is NewRecipeUiAction.FillIngredientInfo -> fillIngredientInfo(id = action.id, ingredient = action.ingredient)
            }
        }

    private fun confirmNewFood() {
        val formattedList = mutableListOf<IngredientInput>()
        for (ingredient in _uiState.value.ingredientsList) {
            if (ingredient != IngredientInput()) formattedList.add(ingredient)
        }
        if (formattedList.size > 0 && formattedList.all { it.isValidIngredient })
            viewModelScope.launch {
                repository.insert(
                    Food(
                        name = _uiState.value.recipeName,
                        protein = formattedList.sumOf { it.protein.toDouble() * it.mass.toInt() / 100.0 },
                        fat = formattedList.sumOf { it.fat.toDouble() * it.mass.toInt() / 100.0 },
                        carbs = formattedList.sumOf { it.carbs.toDouble() * it.mass.toInt() / 100.0 },
                        imageUrl = null
                    )
                )
                formattedList.forEach {
                    repository.insertIngredient(
                        Ingredient(
                            name = it.name,
                            protein = it.protein.toDouble(),
                            fat = it.fat.toDouble(),
                            carbs = it.carbs.toDouble()
                        )
                    )
                }
            }
    }

    private fun increaseNumberOfIngredients() {
        val emptyIngredient = IngredientInput()
        if (_uiState.value.ingredientsList.size <= 20) _uiState.update {
            it.copy(ingredientsList = it.ingredientsList.apply { add(emptyIngredient) })
        }
    }

    private fun decreaseNumberOfIngredients() {
        if (_uiState.value.ingredientsList.size > 1) _uiState.update {
            it.copy(ingredientsList = it.ingredientsList.apply { removeLast() })
        }
    }

    private fun onIngredientNameChanged(id: Int, name: String) {
        _uiState.update {
            val updatedList = it.ingredientsList.apply {
                it.ingredientsList[id] = it.ingredientsList.get(id).copy(name = name)
            }
            val ingredientsFoundList = getIngredient(name)
            it.copy(
                ingredientsList = updatedList,
                ingredientsFoundList = ingredientsFoundList
            )
        }
    }

    private fun onIngredientNutrientChanged(id: Int, input: String, nutrient: Nutrient) {
        if (input.matches(Regex(DOUBLE_PATTERN))) {
            _uiState.update {
                val updatedList = when (nutrient) {
                        Nutrient.Protein -> it.ingredientsList[id].copy(protein = input)
                        Nutrient.Fat -> it.ingredientsList[id].copy(fat = input)
                        Nutrient.Carbs -> it.ingredientsList[id].copy(carbs = input)
                    }
                it.copy(ingredientsList = it.ingredientsList.apply {
                    it.ingredientsList[id] = updatedList
                } )
            }
        }
    }

    private fun onIngredientMassChanged(id: Int, text: String) {
        val mass = text.toIntOrNull() ?: return
        _uiState.update {
            it.copy(ingredientsList = it.ingredientsList.apply {
                it.ingredientsList[id] = it.ingredientsList.get(id).copy(mass = mass.toString())
            })
        }
    }

    private fun onRecipeNameChanged(name: String) {
        _uiState.update {
            it.copy(recipeName = name)
        }
    }

    private fun onRecipeMassChanged(text: String) {
        val mass = text.toIntOrNull() ?: return
        _uiState.update {
            it.copy(totalMass = mass)
        }
    }

    private fun deleteIngredient(id: Int) {
        _uiState.update {
            val updatedList = it.ingredientsList.apply {
                if (it.ingredientsList[id] == IngredientInput() && it.ingredientsList.size > 1)
                    it.ingredientsList.removeAt(id)
                else it.ingredientsList[id] = IngredientInput()
            }
            it.copy(ingredientsList = updatedList)
        }
    }

    private fun getIngredient(name: String): List<Ingredient> {
        var ingredientList = listOf<Ingredient>()
        viewModelScope.launch {
            ingredientList = repository.getIngredient(name)
        }
        return ingredientList
    }

    private fun fillIngredientInfo(id: Int, ingredient: Ingredient) {
        _uiState.update {
            val updatedList = it.ingredientsList.apply {
                it.ingredientsList[id] = it.ingredientsList[id].copy(
                    name = ingredient.name,
                    protein = ingredient.protein.toString(),
                    fat = ingredient.fat.toString(),
                    carbs = ingredient.carbs.toString()
                )
            }
            it.copy(
                ingredientsList = updatedList
            )
        }
    }
}