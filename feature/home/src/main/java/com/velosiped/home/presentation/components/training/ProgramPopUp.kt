package com.velosiped.home.presentation.components.training

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.home.R
import com.velosiped.ui.components.DialogIconButton
import com.velosiped.ui.components.popupwindow.BasePopUpWindow
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.ui.R as coreR

@Composable
fun ProgramPopUp(
    onProgramManager: () -> Unit,
    onStatistics: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasePopUpWindow(
        options = {
            DialogIconButton(
                painter = painterResource(coreR.drawable.statistics),
                text = stringResource(id = R.string.main_screen_dest_stats),
                onClick = { onStatistics() }
            )
            DialogIconButton(
                painter = painterResource(coreR.drawable.manage_program),
                text = stringResource(id = R.string.main_screen_dest_program_manager),
                onClick = { onProgramManager() }
            )
        },
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        ProgramPopUp(
            onProgramManager = {  },
            onStatistics = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}