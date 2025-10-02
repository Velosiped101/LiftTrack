package com.velosiped.ui.components.image

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.DialogIconButton
import com.velosiped.ui.components.popupwindow.BasePopUpWindow
import com.velosiped.ui.theme.CustomTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageOperationsDialog(
    onTakeNewPhoto: () -> Unit,
    onDeleteCurrentPhoto: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        BasePopUpWindow(
            options = {
                DialogIconButton(
                    painter = painterResource(R.drawable.camera),
                    text = stringResource(R.string.take_a_new_photo),
                    onClick = onTakeNewPhoto
                )
                DialogIconButton(
                    painter = painterResource(R.drawable.delete),
                    text = stringResource(R.string.delete_current_photo),
                    onClick = onDeleteCurrentPhoto
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, backgroundColor = 5)
@Composable
private fun Preview() {
    CustomTheme {
        ImageOperationsDialog(
            onTakeNewPhoto = { },
            onDeleteCurrentPhoto = { },
            onDismiss = { },
            modifier = Modifier.fillMaxWidth()
        )
    }
}