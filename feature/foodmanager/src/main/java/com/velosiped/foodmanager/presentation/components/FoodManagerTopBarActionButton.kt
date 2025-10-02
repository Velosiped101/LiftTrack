package com.velosiped.foodmanager.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.CustomIcon

@Composable
fun FoodManagerTopBarActionButton(
    inDeleteMode: Boolean,
    atMainPage: Boolean,
    confirmButtonIsVisible: Boolean,
    onDelete: () -> Unit,
    onAdd: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = Pair(inDeleteMode, atMainPage),
        modifier = modifier
    ) { (inDeleteMode, onMainPage) ->
        if (!onMainPage) {
            AnimatedVisibility(visible = confirmButtonIsVisible) {
                IconButton(onClick = onSave) {
                    CustomIcon(painter = painterResource(id = R.drawable.confirm))
                }
            }
        } else if (inDeleteMode) {
            IconButton(onClick = onDelete) {
                CustomIcon(painter = painterResource(id = R.drawable.delete))
            }
        } else {
            IconButton(onClick = onAdd) {
                CustomIcon(painterResource(id = R.drawable.add_plus))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InDeleteMode() {
    FoodManagerTopBarActionButton(
        inDeleteMode = true,
        atMainPage = true,
        confirmButtonIsVisible = false,
        onDelete = {  },
        onAdd = {  },
        onSave = {  }
    )
}

@Preview(showBackground = true)
@Composable
private fun OnMainPage() {
    FoodManagerTopBarActionButton(
        inDeleteMode = false,
        atMainPage = true,
        confirmButtonIsVisible = true,
        onDelete = {  },
        onAdd = {  },
        onSave = {  }
    )
}

@Preview(showBackground = true)
@Composable
private fun OnEditPage() {
    FoodManagerTopBarActionButton(
        inDeleteMode = false,
        atMainPage = false,
        confirmButtonIsVisible = true,
        onDelete = { },
        onAdd = { },
        onSave = { }
    )
}