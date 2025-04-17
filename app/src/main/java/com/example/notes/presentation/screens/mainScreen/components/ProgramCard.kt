package com.example.notes.presentation.screens.mainScreen.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.notes.presentation.screens.mainScreen.MainScreenUiAction
import com.example.notes.presentation.screens.mainScreen.MainScreenUiState

@Composable
fun ProgramCard(
    uiState: MainScreenUiState,
    uiAction: (MainScreenUiAction) -> Unit,
    navigateToProgramManager: () -> Unit,
    navigateToProgramExec: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            Modifier.padding(4.dp)
        ) {
            Row {
                Text(text = if (uiState.isRestDay) "Rest day" else "Training day", fontSize = 10.sp)
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    // TODO: Image
                    if (uiState.isRestDay.not())
                        IconButton(onClick = navigateToProgramExec) {
                            Text(text = "start")
                        }
                }
            }
            Card(
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.dp, Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                // TODO: Graph field
            }
            Text(
                modifier = Modifier
                    .align(Alignment.End)
                    .clickable { navigateToProgramManager() },
                text = "Manage your program"
            )
        }
    }
}