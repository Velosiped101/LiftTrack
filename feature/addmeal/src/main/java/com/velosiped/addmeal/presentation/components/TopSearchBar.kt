package com.velosiped.addmeal.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.utility.extensions.HUNDRED

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
            unfocusedBorderColor = CustomTheme.colors.searchBarColors.focusedOutlineColor,
            focusedTextColor = CustomTheme.colors.searchBarColors.focusedTextColor,
            unfocusedTextColor = CustomTheme.colors.searchBarColors.unfocusedTextColor
        ),
        textStyle = CustomTheme.typography.textFieldInput,
        trailingIcon = {
            AnimatedVisibility(
                visible = input.isNotBlank(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                CustomIcon(
                    painter = painterResource(id = R.drawable.delete_from_list),
                    onClick = { onInputClear() },
                    modifier = Modifier.scale(Float.HALF)
                )
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