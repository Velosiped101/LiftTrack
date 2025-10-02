package com.velosiped.newrecipe.presentation.components.confirmationpage

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.model.textfieldvalidator.TextFieldState
import com.velosiped.ui.utils.getTakePhotoLauncher
import com.velosiped.newrecipe.R
import com.velosiped.ui.components.CustomTextField
import com.velosiped.ui.components.image.FoodImage
import com.velosiped.ui.components.image.ImageOperationsDialog
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun NewRecipeConfirmationPage(
    tempFileUri: Uri?,
    currentImageUri: Uri?,
    useAutoCalcMass: Boolean,
    recipeNameFieldState: TextFieldState,
    massFieldState: TextFieldState,
    onPhotoTaken: () -> Unit,
    onRecipeNameChange: (String) -> Unit,
    onAutoMassCalcChange: (Boolean) -> Unit,
    onDeleteCurrentPhoto: () -> Unit,
    onRecipeMassChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val launchCamera = getTakePhotoLauncher(
        uri = tempFileUri,
        context = context,
        onSuccess = onPhotoTaken
    )
    var dialogIsActive by remember { mutableStateOf(false) }
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_12)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ) {
        FoodImage(
            uri = currentImageUri,
            modifier = Modifier.clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                if (currentImageUri != null) {
                    dialogIsActive = true
                } else { launchCamera() }
            }
        )
        CustomTextField(
            value = recipeNameFieldState.text,
            onValueChange = { onRecipeNameChange(it) },
            underlineHint = stringResource(id = R.string.new_recipe_name)
        )
        NewRecipeMassSelector(
            massFieldState = massFieldState,
            useAutoCalcMass = useAutoCalcMass,
            onMassInputChange = onRecipeMassChange,
            onAutoMassCalcChange = onAutoMassCalcChange
        )
    }
    if (dialogIsActive) {
        ImageOperationsDialog(
            onTakeNewPhoto = {
                launchCamera()
                dialogIsActive = false
            },
            onDeleteCurrentPhoto = {
                onDeleteCurrentPhoto()
                dialogIsActive = false
            },
            onDismiss = { dialogIsActive = false }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        NewRecipeConfirmationPage(
            tempFileUri = null,
            currentImageUri = null,
            useAutoCalcMass = false,
            recipeNameFieldState = TextFieldState(text = "Rice"),
            massFieldState = TextFieldState(text = "20"),
            onPhotoTaken = { },
            onRecipeNameChange = { },
            onAutoMassCalcChange = { },
            onDeleteCurrentPhoto = { },
            onRecipeMassChange = { },
            modifier = Modifier.fillMaxSize()
        )
    }
}