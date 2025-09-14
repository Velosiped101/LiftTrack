package com.velosiped.notes.presentation.screens.diet.addmeal.components.pickedfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.notes.R
import com.velosiped.notes.presentation.screens.components.CustomIcon
import com.velosiped.notes.ui.theme.CustomTheme
import com.velosiped.notes.utils.HALF
import com.velosiped.notes.utils.ONE
import com.velosiped.notes.utils.QUARTER

@Composable
fun PickedFoodItem(
    name: String,
    mass: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.space_by_4)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = name,
            maxLines = Int.ONE,
            overflow = TextOverflow.Ellipsis,
            style = CustomTheme.typography.listItemName,
            modifier = Modifier.weight(Float.ONE)
        )
        CustomIcon(
            painter = painterResource(id = R.drawable.edit),
            onClick = onEdit
        )
        Text(
            text = mass.toString() + stringResource(id = R.string.g),
            maxLines = Int.ONE,
            overflow = TextOverflow.Ellipsis,
            style = CustomTheme.typography.listItemName,
            modifier = Modifier.weight(Float.QUARTER)
        )
        CustomIcon(
            painter = painterResource(id = R.drawable.delete_from_list),
            onClick = onDelete,
            modifier = Modifier.scale(Float.HALF)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        PickedFoodItem(
            name = "Meat with unrealistically long name",
            mass = 2000,
            onEdit = { },
            onDelete = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(R.dimen.picked_food_item_height))
        )
    }
}