package com.velosiped.notes.presentation.screens.diet.addmeal.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.HUNDRED
import com.velosiped.notes.utils.ONE_THIRD

@Composable
fun TopSearchBar(
    input: String,
    onInputClear: () -> Unit,
    onInputChange: (String) -> Unit
) {
    OutlinedTextField(
        value = input,
        onValueChange = { onInputChange(it) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Int.HUNDRED),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = CustomTheme.colors.searchBarColors.focusedOutlineColor,
            unfocusedBorderColor = CustomTheme.colors.searchBarColors.focusedOutlineColor
        ),
        textStyle = CustomTheme.typography.textFieldInput,
        trailingIcon = {
            AnimatedVisibility(
                visible = input.isNotBlank(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                IconButton(
                    onClick = { onInputClear() },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.delete_from_list),
                        contentDescription = null,
                        tint = CustomTheme.colors.iconsTintColor,
                        modifier = Modifier.scale(Float.ONE_THIRD)
                    )
                }
            }
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_search_24),
                contentDescription = null,
                tint = CustomTheme.colors.iconsTintColor
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun FilledSearchBarPreview() {
    CustomTheme {
        TopSearchBar(
            input = "potato",
            onInputClear = { },
            onInputChange = { }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptySearchBarPreview() {
    CustomTheme {
        TopSearchBar(
            input = "",
            onInputClear = { },
            onInputChange = { }
        )
    }
}