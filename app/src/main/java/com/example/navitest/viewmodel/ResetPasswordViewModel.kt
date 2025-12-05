package com.example.navitest.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navitest.api.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ResetPasswordUiState(
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val successMessage: String = ""
)

class ResetPasswordViewModel : ViewModel() {
    private val authRepository = AuthRepository()
    private val _uiState = MutableStateFlow(ResetPasswordUiState())
    val uiState: StateFlow<ResetPasswordUiState> = _uiState.asStateFlow()

    fun updateNewPassword(password: String) {
        _uiState.value = _uiState.value.copy(
            newPassword = password,
            isError = false,
            errorMessage = ""
        )
    }

    fun updateConfirmPassword(password: String) {
        _uiState.value = _uiState.value.copy(
            confirmPassword = password,
            isError = false,
            errorMessage = ""
        )
    }

    fun resetPassword(token: String) {
        viewModelScope.launch {
            if (_uiState.value.newPassword != _uiState.value.confirmPassword) {
                _uiState.value = _uiState.value.copy(
                    isError = true,
                    errorMessage = "Las contraseñas no coinciden"
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                isError = false,
                errorMessage = "",
                successMessage = ""
            )

            val result = authRepository.resetPasswordWithToken(token, _uiState.value.newPassword)
            
            result.fold(
                onSuccess = { response ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        successMessage = "✅ Contraseña actualizada exitosamente. Ahora puedes iniciar sesión."
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = error.message ?: "Error al restablecer la contraseña"
                    )
                }
            )
        }
    }
}
