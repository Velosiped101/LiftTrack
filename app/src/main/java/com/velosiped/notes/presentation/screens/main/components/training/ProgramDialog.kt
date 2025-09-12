package com.velosiped.notes.presentation.screens.main.components.training

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.BasicPopUpWindow
import com.velosiped.notes.presentation.screens.components.DialogIconButton
import com.velosiped.notes.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramDialog(
    onProgramManager: () -> Unit,
    onStatistics: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        BasicPopUpWindow(
            options = {
                DialogIconButton(
                    iconId = R.drawable.statistics,
                    text = stringResource(id = R.string.statistics),
                    onClick = { onStatistics() }
                )
                DialogIconButton(
                    iconId = R.drawable.manage_program,
                    text = stringResource(id = R.string.manage_program),
                    onClick = { onProgramManager() }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramDialog(
            onProgramManager = {  },
            onStatistics = {  },
            onDismiss = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}