package com.example.navitest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navitest.api.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EmailVerificationUiState(
    val code: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isVerified: Boolean = false,
    val canResend: Boolean = true,
    val timeUntilResend: Int = 0
)

class EmailVerificationViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val _uiState = MutableStateFlow(EmailVerificationUiState())
    val uiState: StateFlow<EmailVerificationUiState> = _uiState.asStateFlow()

    fun updateCode(code: String) {
        _uiState.value = _uiState.value.copy(
            code = code,
            isError = false,
            errorMessage = ""
        )
    }

    fun verifyEmail(token: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isError = false,
                errorMessage = ""
            )

            val result = authRepository.verifyEmail(token, _uiState.value.code)
            
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isVerified = true
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = error.message ?: "Error al verificar el código"
                    )
                }
            )
        }
    }

    fun resendVerificationCode(token: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isError = false,
                errorMessage = "",
                canResend = false
            )

            val result = authRepository.resendVerificationCode(token)
            
            result.fold(
                onSuccess = {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        code = ""
                    )
                    startResendTimer()
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = error.message ?: "Error al reenviar el código",
                        canResend = true
                    )
                }
            )
        }
    }

    private fun startResendTimer() {
        viewModelScope.launch {
            for (i in 60 downTo 1) {
                _uiState.value = _uiState.value.copy(timeUntilResend = i)
                delay(1000)
            }
            _uiState.value = _uiState.value.copy(
                canResend = true,
                timeUntilResend = 0
            )
        }
    }
}
