package com.velosiped.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.divider.CustomHorizontalDivider
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    underlineHint: String? = null,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    readOnly: Boolean = false,
    alignToStart: Boolean = false
) {
    val textStyle =
        if (alignToStart) CustomTheme.typography.screenMessageMedium.copy(textAlign = TextAlign.Start)
        else CustomTheme.typography.screenMessageMedium
    val textFieldAlignment = if (alignToStart) Alignment.CenterStart else Alignment.Center
    val underlineHintAlignment = if (alignToStart) Alignment.Start else Alignment.CenterHorizontally

    BasicTextField(
        value = TextFieldValue(
            text = value,
            selection = TextRange(value.length)
        ),
        onValueChange = { onValueChange(it.text) },
        singleLine = true,
        textStyle = textStyle.copy(color = CustomTheme.colors.textFieldColors.textColor),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        cursorBrush = SolidColor(CustomTheme.colors.textFieldColors.cursorColor),
        decorationBox = {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val inputFieldModifier =
                    if (readOnly) Modifier.background(CustomTheme.colors.textFieldColors.readOnlyColor)
                    else Modifier
                Box(
                    contentAlignment = textFieldAlignment,
                    modifier = inputFieldModifier.fillMaxWidth()
                ) {
                    it()
                    if (readOnly) {
                        CustomIcon(
                            painter = painterResource(R.drawable.lock),
                            modifier = Modifier
                                .background(CustomTheme.colors.textFieldColors.readOnlyColor)
                                .scale(Float.HALF)
                                .align(Alignment.CenterEnd)
                        )
                    }
                }
                CustomHorizontalDivider()
                underlineHint?.let {
                    Text(
                        text = it,
                        style = CustomTheme.typography.underlineHint,
                        color = CustomTheme.colors.primaryTextColor,
                        modifier = Modifier.align(underlineHintAlignment)
                    )
                }
            }
        },
        readOnly = readOnly,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun ReadOnlyTextPreview() {
    CustomTheme {
        CustomTextField(
            value = "something gggggggggggggggggggggggggggggggggggggggggggggggggggg",
            onValueChange = { },
            readOnly = true,
            alignToStart = true,
            underlineHint = "Name",
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
            underlineHint = "mass, g",
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.space_by_4))
        )
    }
}