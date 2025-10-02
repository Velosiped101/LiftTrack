package com.velosiped.home.presentation.components.training

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.R
import com.velosiped.ui.R as coreR
import com.velosiped.ui.components.DialogIconButton
import com.velosiped.ui.components.popupwindow.BasePopUpWindow
import com.velosiped.ui.components.popupwindow.PopUpWindowTextHeader
import com.velosiped.ui.theme.CustomTheme

@Composable
fun ProgramExecConfirmationPopUp(
    onStartNew: () -> Unit,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasePopUpWindow(
        options = {
            DialogIconButton(
                painter = painterResource(coreR.drawable.resume_session),
                text = stringResource(id = R.string.main_screen_training_continue_previous),
                onClick = { onContinue() }
            )
            DialogIconButton(
                painter = painterResource(coreR.drawable.new_session),
                text = stringResource(id = R.string.main_screen_training_start_new),
                onClick = { onStartNew() }
            )
        },
        header = {
            PopUpWindowTextHeader(
                text = stringResource(id = R.string.main_screen_training_program_changed)
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramExecConfirmationPopUp(
            onStartNew = { },
            onContinue = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}