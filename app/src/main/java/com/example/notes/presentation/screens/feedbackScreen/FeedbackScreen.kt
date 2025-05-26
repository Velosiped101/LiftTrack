package com.example.notes.presentation.screens.feedbackScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.notes.R
import com.example.notes.ui.theme.CustomTheme
import com.example.notes.ui.theme.screenMessageMedium
import com.example.notes.ui.theme.screenMessageSmall
import com.example.notes.ui.theme.topBarHeadline
import com.example.notes.ui.theme.underlineHint
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun FeedbackScreen(
    uiState: FeedbackUiState,
    uiAction: (FeedbackUiAction) -> Unit,
    sendingCompleted: SharedFlow<Boolean>,
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val blur by animateIntAsState(
        targetValue = if (uiState.isSending) 10 else 0,
        label = ""
    )
    LaunchedEffect(key1 = Unit) {
        sendingCompleted.collect { isCompleted ->
            val toastText = if (isCompleted) context.getString(R.string.feedback_success)
            else context.getString(R.string.feedback_failure)
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
            onNavigateBack()
        }
    }
    Box(modifier = Modifier.fillMaxSize()){
        Scaffold(
            topBar = {
                TopBar {
                    onNavigateBack()
                }
            },
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember {
                        MutableInteractionSource()
                    }
                ) {
                    focusManager.clearFocus()
                }
                .blur(blur.dp)
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                StartScreen(
                    uiState = uiState,
                    uiAction = uiAction
                )
            }
        }
        AnimatedVisibility(
            visible = uiState.isSending,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .fillMaxWidth(.5f)
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StartScreen(
    uiState: FeedbackUiState,
    uiAction: (FeedbackUiAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val validMessageLength = 50
    var sendingEnabled = uiState.content.length >= validMessageLength
    val counterColor by animateColorAsState(
        targetValue = if (sendingEnabled) CustomTheme.colors.achievedColor
        else CustomTheme.colors.notAchievedColor, label = ""
    )
    val counterText = "${uiState.content.length}/$validMessageLength"
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxSize()
    ) {
        SingleChoiceSegmentedButtonRow {
            FeedbackType.entries.forEachIndexed { index, type ->
                SegmentedButton(
                    selected = type == uiState.type,
                    onClick = { uiAction(FeedbackUiAction.ChangeType(type)) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = FeedbackType.entries.size
                    )
                ) {
                    Text(
                        text = stringResource(type.textId),
                        style = MaterialTheme.typography.screenMessageSmall
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
        ){
            Text(
                text = counterText,
                style = MaterialTheme.typography.underlineHint.copy(color = counterColor),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 16.dp, bottom = 4.dp))
            OutlinedTextField(
                value = uiState.content,
                onValueChange = { uiAction(FeedbackUiAction.ChangeText(it)) },
                textStyle = MaterialTheme.typography.screenMessageMedium.copy(textAlign = TextAlign.Start),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.outline,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
        AnimatedVisibility(
            visible = uiState.type == FeedbackType.BugReport,
            modifier = Modifier.align(Alignment.End)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.wrapContentSize()
            ) {
                Text(
                    text = stringResource(id = R.string.allow_system_info),
                    style = MaterialTheme.typography.screenMessageSmall
                )
                RadioButton(
                    selected = uiState.allowSystemInformation,
                    onClick = { uiAction(FeedbackUiAction.ChangeSystemInfoAllowed(!uiState.allowSystemInformation)) }
                )
            }
        }
        Button(
            onClick = {
                sendingEnabled = false
                uiAction(FeedbackUiAction.SendFeedback)
            },
            enabled = sendingEnabled
        ) {
            Text(
                text = stringResource(id = R.string.send_feedback),
                style = MaterialTheme.typography.screenMessageSmall
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onNavigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.send_feedback_headline),
                style = MaterialTheme.typography.topBarHeadline,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = { onNavigateBack() }) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.surfaceTint,
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(.5f)
                )
            }
        },
        actions = { Box(modifier = Modifier.size(48.dp)) }
    )
}

@Preview
@Composable
private fun Preview() {
    FeedbackScreen(
        uiState = FeedbackUiState(isSending = false, type = FeedbackType.BugReport),
        uiAction = {},
        sendingCompleted = MutableSharedFlow()
    ) {

    }
}