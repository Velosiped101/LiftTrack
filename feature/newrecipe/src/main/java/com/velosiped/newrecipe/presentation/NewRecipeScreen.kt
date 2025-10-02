package com.velosiped.newrecipe.presentation

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.diet.ingredient.repository.Ingredient
import com.velosiped.newrecipe.R
import com.velosiped.newrecipe.presentation.components.NewRecipeFAB
import com.velosiped.newrecipe.presentation.components.confirmationpage.NewRecipeConfirmationPage
import com.velosiped.newrecipe.presentation.components.editpage.NewRecipeEditPage
import com.velosiped.newrecipe.presentation.components.utils.IngredientInputState
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.components.topbar.BaseTopBar
import com.velosiped.ui.components.topbar.TopBarHeader
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF_CIRCLE_ANGLE
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.TWO
import com.velosiped.utility.extensions.ZERO
import kotlinx.coroutines.launch
import com.velosiped.ui.R as coreR

@Composable
fun NewRecipeScreen(
    ingredients: List<IngredientInputState>,
    ingredientsFound: List<Ingredient>,
    recipeNameFieldState: TextFieldState,
    tempFileUri: Uri?,
    currentImageUri: Uri?,
    massFieldInputState: TextFieldState,
    useAutoMass: Boolean,
    onFocusChanged: (IngredientInputState, FocusState) -> Unit,
    onPhotoTaken: () -> Unit,
    onRecipeNameChange: (String) -> Unit,
    onAutoMassCalcChange: (Boolean) -> Unit,
    onDeleteCurrentPhoto: () -> Unit,
    onRecipeMassChange: (String) -> Unit,
    onDeleteIngredient: (IngredientInputState) -> Unit,
    onAutoCompleteIngredientInput: (IngredientInputState, Ingredient) -> Unit,
    onNameChange: (IngredientInputState, String) -> Unit,
    onProteinChange: (IngredientInputState, String) -> Unit,
    onFatChange: (IngredientInputState, String) -> Unit,
    onCarbsChange: (IngredientInputState, String) -> Unit,
    onMassChange: (IngredientInputState, String) -> Unit,
    onConfirm: () -> Unit,
    onDecrease: () -> Unit,
    onIncrease: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val ingredientsPage = Int.ZERO
    val confirmationPage = Int.ONE
    val pageCount = Int.TWO
    val pagerState = rememberPagerState(initialPage = ingredientsPage, pageCount = {pageCount})
    val scope = rememberCoroutineScope()
    val saveEnabled =
        pagerState.currentPage == confirmationPage &&
        recipeNameFieldState.text.isNotBlank() &&
        massFieldInputState.text.isNotBlank() &&
        massFieldInputState.text.toInt() != Int.ZERO
    val focusManager = LocalFocusManager.current
    fun handleBackPress() {
        when (pagerState.currentPage) {
            ingredientsPage -> onNavigateBack()
            else -> scope.launch { pagerState.animateScrollToPage(ingredientsPage) }
        }
    }
    BackHandler { handleBackPress() }
    Scaffold(
        topBar = {
            BaseTopBar(
                onNavigateBack = { handleBackPress() },
                header = {
                    TopBarHeader(text = stringResource(id = R.string.new_recipe_headline))
                },
                action = {
                    AnimatedVisibility(visible = saveEnabled) {
                        CustomIcon(
                            painter = painterResource(id = coreR.drawable.confirm),
                            onClick = onConfirm
                        )
                    }
                }
            )
        },
        containerColor = CustomTheme.colors.mainBackgroundColor,
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { focusManager.clearFocus() }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                when (page) {
                    ingredientsPage -> NewRecipeEditPage(
                        ingredients = ingredients,
                        ingredientsFound = ingredientsFound,
                        onFocusChanged = onFocusChanged,
                        onIngredientDelete = onDeleteIngredient,
                        onAutoCompleteIngredientInput = onAutoCompleteIngredientInput,
                        onNameChange = onNameChange,
                        onProteinChange = onProteinChange,
                        onFatChange = onFatChange,
                        onCarbsChange = onCarbsChange,
                        onMassChange = onMassChange,
                        onDecrease = onDecrease,
                        onIncrease = onIncrease,
                        modifier = Modifier.fillMaxSize()
                    )
                    confirmationPage -> NewRecipeConfirmationPage(
                        recipeNameFieldState = recipeNameFieldState,
                        tempFileUri = tempFileUri,
                        currentImageUri = currentImageUri,
                        massFieldState = massFieldInputState,
                        useAutoCalcMass = useAutoMass,
                        onPhotoTaken = onPhotoTaken,
                        onRecipeNameChange = onRecipeNameChange,
                        onAutoMassCalcChange = onAutoMassCalcChange,
                        onDeleteCurrentPhoto = onDeleteCurrentPhoto,
                        onRecipeMassChange = onRecipeMassChange,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            AnimatedVisibility(
                visible = ingredients.all { it.noEmptyFields },
                enter = scaleIn(),
                exit = scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(
                        end = dimensionResource(R.dimen.new_recipe_floating_button_padding_end),
                        bottom = dimensionResource(R.dimen.new_recipe_floating_button_padding_bottom)
                    )
            ) {
                val rotationAngle by animateFloatAsState(
                    targetValue = when (pagerState.currentPage) {
                        ingredientsPage -> Float.HALF_CIRCLE_ANGLE
                        else -> Float.ZERO
                    }
                )
                NewRecipeFAB(
                    onClick = {
                        when (pagerState.currentPage) {
                            ingredientsPage -> scope.launch {
                                pagerState.animateScrollToPage(
                                    confirmationPage
                                )
                            }

                            confirmationPage -> scope.launch {
                                pagerState.animateScrollToPage(
                                    ingredientsPage
                                )
                            }
                        }
                    },
                    rotationAngle = rotationAngle
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CustomTheme {
        NewRecipeScreen(
            ingredients = listOf(IngredientInputState(
                nameFieldState = TextFieldState(text = "Name"),
                proteinFieldState = TextFieldState(text = "8"),
                fatFieldState = TextFieldState(text = "16"),
                carbsFieldState = TextFieldState(text = "24"),
                massFieldState = TextFieldState(text = "200"),
                readOnly = true
            )),
            ingredientsFound = emptyList(),
            recipeNameFieldState = TextFieldState(),
            tempFileUri = null,
            currentImageUri = null,
            massFieldInputState = TextFieldState(),
            useAutoMass = true,
            onFocusChanged = { _, _ -> },
            onPhotoTaken = {  },
            onRecipeNameChange = {  },
            onAutoMassCalcChange = {  },
            onDeleteCurrentPhoto = {  },
            onRecipeMassChange = {  },
            onDeleteIngredient = {  },
            onAutoCompleteIngredientInput = { _, _ -> },
            onNameChange = { _, _ -> },
            onProteinChange = { _, _ -> },
            onFatChange = { _, _ -> },
            onCarbsChange = { _, _ -> },
            onMassChange = { _, _ -> },
            onConfirm = {  },
            onDecrease = {  },
            onIncrease = {  }
        ) { }
    }
}