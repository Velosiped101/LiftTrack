package com.velosiped.programmanager.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programmanager.R
import com.velosiped.ui.components.CustomOutlinedButton
import com.velosiped.ui.components.DialogIconButton
import com.velosiped.ui.components.popupwindow.BasePopUpWindow
import com.velosiped.ui.components.popupwindow.PopUpWindowTextHeader
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.EMPTY
import com.velosiped.ui.R as coreR

@Composable
fun DeleteProgramPopUp(
    onDropCurrentDayProgram: () -> Unit,
    onDropProgram: () -> Unit,
    modifier: Modifier = Modifier
) {
    var intent: (() -> Unit)? by rememberSaveable { mutableStateOf(null) }
    var intentTextResId: Int? by rememberSaveable { mutableStateOf(null) }

    if (intent == null) {
        BasePopUpWindow(
            options = {
                DialogIconButton(
                    onClick = {
                        intent = { onDropProgram() }
                        intentTextResId = R.string.program_manager_confirm_drop
                    },
                    painter = painterResource(id = coreR.drawable.drop),
                    text = stringResource(id = R.string.program_manager_clear_program)
                )
                DialogIconButton(
                    onClick = {
                        intent = { onDropCurrentDayProgram() }
                        intentTextResId = R.string.program_manager_confirm_drop_for_day
                    },
                    painter = painterResource(id = coreR.drawable.drop_current),
                    text = stringResource(id = R.string.program_manager_clear_program_for_day)
                )
            },
            modifier = modifier
        )
    } else {
        BasePopUpWindow(
            header = {
                PopUpWindowTextHeader(text = stringResource(R.string.program_manager_are_you_sure))
            },
            options = {
                CustomOutlinedButton(
                    onClick = { intent?.invoke() },
                    text = intentTextResId?.let { stringResource(it) } ?: String.EMPTY,
                )
            },
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        DeleteProgramPopUp(
            onDropCurrentDayProgram = { },
            onDropProgram = { }
        )
    }
}