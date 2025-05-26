package com.example.notes.presentation.screens.feedbackScreen

import com.example.notes.R
import com.example.notes.utils.EMPTY_STRING

data class FeedbackUiState(
    val isSending: Boolean = false,
    val type: FeedbackType = FeedbackType.Message,
    val content: String = EMPTY_STRING,
    val allowSystemInformation: Boolean = false
)

enum class FeedbackType(val textId: Int) {
    Message(R.string.feedback_message_type),
    BugReport(R.string.feedback_bug_report_type)
}