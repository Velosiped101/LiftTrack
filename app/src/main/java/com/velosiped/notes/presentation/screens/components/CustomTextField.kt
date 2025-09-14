package com.velosiped.notes.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.ui.theme.CustomTheme

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = TextFieldValue(
            text = value,
            selection = TextRange(value.length)
        ),
        onValueChange = { onValueChange(it.text) },
        singleLine = true,
        textStyle = CustomTheme.typography.screenMessageMedium,
        keyboardOptions = KeyboardOptions(showKeyboardOnFocus = true),
        cursorBrush = SolidColor(CustomTheme.colors.textFieldColors.cursorColor),
        decorationBox = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                it()
                CustomHorizontalDivider()
            }
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun TextPreview() {
    CustomTheme {
        CustomTextField(
            value = "something",
            onValueChange = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_4))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NumberPreview() {
    CustomTheme {
        CustomTextField(
            value = "56.0",
            onValueChange = {  },
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_4))
        )
    }
}