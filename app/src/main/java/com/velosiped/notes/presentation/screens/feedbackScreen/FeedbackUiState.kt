package com.velosiped.notes.presentation.screens.feedbackScreen

import com.velosiped.notes.R
import com.velosiped.notes.utils.Constants

data class FeedbackUiState(
    val isSending: Boolean = false,
    val type: FeedbackType = FeedbackType.Message,
    val content: String = Constants.EMPTY_STRING,
    val allowSystemInformation: Boolean = false
)

enum class FeedbackType(val textId: Int) {
    Message(R.string.feedback_message_type),
    BugReport(R.string.feedback_bug_report_type)
}