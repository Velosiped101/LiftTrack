package com.velosiped.notes.presentation.screens.feedbackScreen

sealed interface FeedbackUiAction {
    data class ChangeType(val type: FeedbackType): FeedbackUiAction
    data class ChangeText(val text: String): FeedbackUiAction
    data class ChangeSystemInfoAllowed(val allowSystemInformation: Boolean): FeedbackUiAction
    data object SendFeedback: FeedbackUiAction
}