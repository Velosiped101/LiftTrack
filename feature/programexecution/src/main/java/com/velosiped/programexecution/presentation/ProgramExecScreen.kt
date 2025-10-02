package com.velosiped.programexecution.presentation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.velosiped.programexecution.R
import com.velosiped.programexecution.presentation.components.PageNavigationButton
import com.velosiped.programexecution.presentation.components.confirmationpage.ConfirmationPage
import com.velosiped.programexecution.presentation.components.programpage.ProgramPage
import com.velosiped.training.traininghistory.repository.TrainingHistory
import com.velosiped.ui.components.topbar.BaseTopBar
import com.velosiped.ui.components.topbar.TopBarHeader
import com.velosiped.ui.theme.CustomTheme
import com.velosiped.utility.extensions.ONE
import com.velosiped.utility.extensions.ZERO
import kotlinx.coroutines.launch
import com.velosiped.ui.R as coreR

@Composable
fun ProgramExecScreen(
    trainingHistory: List<TrainingHistory>,
    showWeightIncreaseHintList: List<Boolean>,
    initialWeightList: List<Float>,
    onUpdateStoredProgress: () -> Unit,
    onConfirm: () -> Unit,
    onRepsChange: (Float, Int) -> Unit,
    onWeightChange: (Float, Int) -> Unit,
    onNavigateBack: () -> Unit
) {
    var navigateBackRequested by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = navigateBackRequested) {
        if (navigateBackRequested) {
            onUpdateStoredProgress()
            onNavigateBack()
        }
    }
    BackHandler(enabled = !navigateBackRequested) {
        navigateBackRequested = true
    }
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(
        initialPage = Int.ZERO,
        pageCount = { trainingHistory.size + Int.ONE }
    )
    Scaffold(
        topBar = {
            BaseTopBar(
                header = {
                    TopBarHeader(text = stringResource(R.string.program_exec_headline))
                },
                onNavigateBack = {
                    if (!navigateBackRequested) navigateBackRequested = true
                }
            )
        },
        containerColor = CustomTheme.colors.mainBackgroundColor
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(dimensionResource(coreR.dimen.space_by_8))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                HorizontalPager(
                    state = pagerState,
                    userScrollEnabled = false,
                    modifier = Modifier.weight(Float.ONE)
                ) { index ->
                    when (index) {
                        in trainingHistory.indices -> {
                            ProgramPage(
                                trainingHistoryItem = trainingHistory[index],
                                showWeightIncreaseHint = showWeightIncreaseHintList[index],
                                initialWeight = initialWeightList[index],
                                onRepsChange = { onRepsChange(it, index) },
                                onWeightChange = { onWeightChange(it, index) }
                            )
                        }
                        else -> ConfirmationPage(
                            trainingHistory = trainingHistory,
                            onConfirm = onConfirm
                        )
                    }
                }
                LinearProgressIndicator(
                    progress = { pagerState.currentPage.toFloat() / trainingHistory.size },
                    color = CustomTheme.colors.linearProgressIndicatorColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(coreR.dimen.space_by_8))
                )
            }
            PageNavigationButton(
                isNextButton = true,
                isVisible = pagerState.currentPage < pagerState.pageCount - Int.ONE,
                onClick = {
                    scope.launch {
                        onUpdateStoredProgress()
                        pagerState.animateScrollToPage(pagerState.currentPage + Int.ONE)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(dimensionResource(coreR.dimen.space_by_12))
            )
            PageNavigationButton(
                isNextButton = false,
                isVisible = pagerState.currentPage > Int.ZERO,
                onClick = {
                    scope.launch {
                        onUpdateStoredProgress()
                        pagerState.animateScrollToPage(pagerState.currentPage - Int.ONE)
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(dimensionResource(coreR.dimen.space_by_12))
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    val trainingHistory = listOf(
        TrainingHistory(day = 1, month = 1, year = 1, exercise = "Squat", reps = 4, repsPlanned = 5, weight = 100f),
        TrainingHistory(day = 1, month = 1, year = 1, exercise = "Dead lift", reps = 5, repsPlanned = 5, weight = 120f),
        TrainingHistory(day = 1, month = 1, year = 1, exercise = "Dead lift", reps = 1, repsPlanned = 5, weight = 120f)
    )
    CustomTheme {
        ProgramExecScreen(
            trainingHistory = trainingHistory,
            showWeightIncreaseHintList = trainingHistory.map { false },
            initialWeightList = trainingHistory.map { 0f },
            onUpdateStoredProgress = { },
            onConfirm = { },
            onRepsChange = { _, _ -> },
            onWeightChange = { _, _ -> }
        ) { }
    }
}