package com.velosiped.programmanager.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R as coreR
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF

@Composable
fun ExerciseBottomSheetHeader(
    text: String,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            style = CustomTheme.typography.screenMessageLarge,
            color = CustomTheme.colors.primaryTextColor,
            modifier = Modifier.align(Alignment.Center)
        )
        CustomIcon(
            painter = painterResource(coreR.drawable.back),
            onClick = onNavigateBack,
            modifier = Modifier
                .scale(Float.HALF)
                .align(Alignment.CenterStart)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ExerciseBottomSheetHeader(
            text = "Update",
            onNavigateBack = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}