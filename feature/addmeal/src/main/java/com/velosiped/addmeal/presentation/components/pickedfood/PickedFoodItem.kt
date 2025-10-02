package com.velosiped.addmeal.presentation.components.pickedfood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.velosiped.addmeal.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.QUARTER
import com.velosiped.ui.R as coreR

@Composable
fun PickedFoodItem(
    name: String,
    mass: Int,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(coreR.dimen.space_by_4)),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = name,
            maxLines = Int.ONE,
            overflow = TextOverflow.Ellipsis,
            style = CustomTheme.typography.listItemName,
            color = CustomTheme.colors.primaryTextColor,
            modifier = Modifier.weight(Float.ONE)
        )
        CustomIcon(
            painter = painterResource(id = coreR.drawable.edit),
            onClick = onEdit
        )
        Text(
            text = mass.toString() + stringResource(id = R.string.add_meal_g),
            maxLines = Int.ONE,
            overflow = TextOverflow.Ellipsis,
            style = CustomTheme.typography.listItemName,
            color = CustomTheme.colors.primaryTextColor,
            modifier = Modifier.weight(Float.QUARTER)
        )
        CustomIcon(
            painter = painterResource(id = coreR.drawable.delete_from_list),
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
            name = "Apple pie",
            mass = 200,
            onEdit = {  },
            onDelete = {  },
            modifier = Modifier.fillMaxWidth()
        )
    }
}