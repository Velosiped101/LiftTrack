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
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onStartNew: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        BasicPopUpWindow(
            options = {
                DialogIconButton(
                    iconId = R.drawable.resume_session,
                    text = stringResource(id = R.string.continue_previous),
                    onClick = { onContinue() }
                )
                DialogIconButton(
                    iconId = R.drawable.new_session,
                    text = stringResource(id = R.string.start_new),
                    onClick = { onStartNew() }
                )
            },
            headerText = stringResource(id = R.string.program_changed_message),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ConfirmationDialog(
            onDismiss = { },
            onStartNew = { },
            onContinue = { }
        )
    }
}