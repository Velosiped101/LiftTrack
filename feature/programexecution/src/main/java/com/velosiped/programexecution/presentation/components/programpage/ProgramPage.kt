package com.velosiped.programexecution.presentation.components.programpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programexecution.R
import com.velosiped.programexecution.presentation.components.programpage.repssetter.RepsSetter
import com.velosiped.programexecution.presentation.components.programpage.weightsetter.WeightSetter
import com.velosiped.training.traininghistory.repository.TrainingHistory

@Composable
fun ProgramPage(
    trainingHistoryItem: TrainingHistory,
    initialWeight: Float,
    showWeightIncreaseHint: Boolean,
    onRepsChange: (Float) -> Unit,
    onWeightChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val minWeight = integerResource(R.integer.program_exec_weight_min).toFloat()
    val maxWeight = integerResource(R.integer.program_exec_weight_max).toFloat()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxSize()
    ) {
        RepsSetter(
            exercise = trainingHistoryItem.exercise,
            repsPlanned = trainingHistoryItem.repsPlanned,
            reps = trainingHistoryItem.reps,
            onRepsChange = onRepsChange,
            modifier = Modifier.fillMaxWidth()
        )
        WeightSetter(
            weight = trainingHistoryItem.weight,
            initialWeight = initialWeight,
            showWeightIncreaseHint = showWeightIncreaseHint,
            onIncrease = {
                val updatedWeight = trainingHistoryItem.weight + it
                if (updatedWeight <= maxWeight) {
                    onWeightChange(trainingHistoryItem.weight + it)
                }
            },
            onDecrease = {
                val updatedWeight = trainingHistoryItem.weight - it
                if (updatedWeight >= minWeight) {
                    onWeightChange(trainingHistoryItem.weight - it)
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ProgramPage(
        trainingHistoryItem = TrainingHistory(
            day = 1,
            month = 1,
            year = 1,
            exercise = "Shoulder press",
            reps = 5,
            repsPlanned = 8,
            weight = 30f
        ),
        initialWeight = 27.5f,
        showWeightIncreaseHint = true,
        onRepsChange = {  },
        onWeightChange = {  }
    )
}