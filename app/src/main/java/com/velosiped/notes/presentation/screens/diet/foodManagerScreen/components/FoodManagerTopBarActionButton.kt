package com.velosiped.notes.presentation.screens.diet.foodManagerScreen.components

import androidx.compose.animation.Crossfade
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.CustomIcon

@Composable
fun FoodManagerTopBarActionButton(
    inDeleteMode: Boolean,
    onMainPage: Boolean,
    onDelete: () -> Unit,
    onAdd: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Crossfade(
        targetState = Pair(inDeleteMode, onMainPage),
        modifier = modifier
    ) { (inDeleteMode, onMainPage) ->
        if (!onMainPage) {
            IconButton(onClick = onSave) {
                CustomIcon(painter = painterResource(id = R.drawable.confirm))
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
        onMainPage = true,
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
        onMainPage = true,
        onDelete = {  },
        onAdd = {  },
        onSave = {  }
    )
}

@Preview(showBackground = true)
@Composable
private fun NotOnMainPage() {
    FoodManagerTopBarActionButton(
        inDeleteMode = false,
        onMainPage = false,
        onDelete = { },
        onAdd = { },
        onSave = { }
    )
}