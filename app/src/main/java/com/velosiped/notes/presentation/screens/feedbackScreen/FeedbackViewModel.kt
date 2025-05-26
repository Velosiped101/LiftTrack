package com.velosiped.notes.presentation.screens.feedbackScreen

import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velosiped.notes.data.api.feedbackApi.FeedbackApiService
import com.velosiped.notes.utils.EMPTY_STRING
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val apiService: FeedbackApiService
): ViewModel() {
    private val _uiState: MutableStateFlow<FeedbackUiState> = MutableStateFlow(FeedbackUiState())
        val uiState = _uiState.asStateFlow()
    fun uiAction(action: FeedbackUiAction) {
        when (action){
            is FeedbackUiAction.ChangeSystemInfoAllowed -> changeSystemInfoAllowed(action.allowSystemInformation)
            is FeedbackUiAction.ChangeText -> changeText(action.text)
            is FeedbackUiAction.ChangeType -> changeType(action.type)
            FeedbackUiAction.SendFeedback -> sendFeedback()
        }
    }

    private val _sendingCompleted = MutableSharedFlow<Boolean>()
    val sendingCompleted = _sendingCompleted.asSharedFlow()

    private fun sendFeedback() {
        val systemInfo = if (_uiState.value.allowSystemInformation) getSystemInformation() else EMPTY_STRING
        _uiState.update {
            it.copy(isSending = true)
        }
        viewModelScope.launch {
            val response = apiService.postFeedback(
                type = _uiState.value.type.name,
                content = _uiState.value.content,
                systemInfo = systemInfo
            )
            _sendingCompleted.emit(response.isSuccessful)
        }
    }

    private fun changeText(text: String) {
        _uiState.update {
            it.copy(content = text)
        }
    }

    private fun changeSystemInfoAllowed(systemInfoAllowed: Boolean) {
        _uiState.update {
            it.copy(allowSystemInformation = systemInfoAllowed)
        }
    }

    private fun changeType(type: FeedbackType) {
        _uiState.update {
            it.copy(type = type)
        }
    }

    private fun getSystemInformation(): String = buildString {
        appendLine("Device: ${Build.DEVICE}")
        appendLine("Model: ${Build.MODEL}")
        appendLine("Android version: ${Build.VERSION.RELEASE}")
        appendLine("Api: ${Build.VERSION.SDK_INT}")
    }
}