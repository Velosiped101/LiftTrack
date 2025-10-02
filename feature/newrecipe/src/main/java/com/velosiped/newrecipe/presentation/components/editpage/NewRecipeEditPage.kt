package com.velosiped.newrecipe.presentation.components.editpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.newrecipe.presentation.components.utils.IngredientInputState
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.ui.R as coreR

@Composable
fun NewRecipeEditPage(
    ingredients: List<IngredientInputState>,
    ingredientsFound: List<Ingredient>,
    onFocusChanged: (IngredientInputState, FocusState) -> Unit,
    onIngredientDelete: (IngredientInputState) -> Unit,
    onAutoCompleteIngredientInput: (IngredientInputState, Ingredient) -> Unit,
    onNameChange: (IngredientInputState, String) -> Unit,
    onProteinChange: (IngredientInputState, String) -> Unit,
    onFatChange: (IngredientInputState, String) -> Unit,
    onCarbsChange: (IngredientInputState, String) -> Unit,
    onMassChange: (IngredientInputState, String) -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_8)),
            modifier = Modifier
                .fillMaxWidth()
                .weight(Float.ONE)
                .padding(dimensionResource(coreR.dimen.space_by_8))
        ) {
            items(ingredients, key = { it.tempId }) { ingredient ->
                IngredientItemCard(
                    ingredientInputState = ingredient,
                    ingredientsFound = ingredientsFound,
                    onFocusChanged = { onFocusChanged(ingredient, it) },
                    onDelete = { onIngredientDelete(ingredient) },
                    onNameChange = { onNameChange(ingredient, it) },
                    onProteinChange = { onProteinChange(ingredient, it) },
                    onFatChange = { onFatChange(ingredient, it) },
                    onCarbsChange = { onCarbsChange(ingredient, it) },
                    onMassChange = { onMassChange(ingredient, it) },
                    onAutoCompleteInput = { onAutoCompleteIngredientInput(ingredient, it) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        IngredientsCounter(
            value = ingredients.size,
            onDecrease = onDecrease,
            onIncrease = onIncrease,
            modifier = Modifier.padding(bottom = dimensionResource(coreR.dimen.space_by_8))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NewRecipeEditPage(
            ingredients = listOf(
                IngredientInputState(),
                IngredientInputState(),
                IngredientInputState()
            ),
            ingredientsFound = emptyList(),
            onFocusChanged = { _, _ -> },
            onIngredientDelete = {  },
            onAutoCompleteIngredientInput = { _, _ -> },
            onNameChange = { _, _ -> },
            onProteinChange = { _, _ -> },
            onFatChange = { _, _ -> },
            onCarbsChange = { _, _ -> },
            onMassChange = { _, _ -> },
            onDecrease = {  },
            onIncrease = {  },
            modifier = Modifier.fillMaxSize()
        )
    }
}