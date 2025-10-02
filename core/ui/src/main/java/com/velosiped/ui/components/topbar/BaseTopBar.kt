package com.velosiped.ui.components.topbar

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.ui.R
import com.velosiped.ui.components.CustomIcon
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.HALF

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopBar(
    onNavigateBack: () -> Unit,
    header: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = { header() },
        navigationIcon = {
            IconButton(onNavigateBack) {
                CustomIcon(
                    painter = painterResource(id = R.drawable.back),
                    modifier = Modifier.scale(Float.HALF)
                )
            }
        },
        actions = {
            Row {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(dimensionResource(R.dimen.top_bar_action_box_size))
                ) {
                    action?.invoke()
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = CustomTheme.colors.topBarBackgroundColor
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    CustomTheme {
        BaseTopBar(
            onNavigateBack = { },
            header = { Text("Header") },
            action = { }
        )
    }
}