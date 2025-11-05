
package com.example.navitest.viewmodel

import androidx.lifecycle.ViewModel
import com.example.navitest.model.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isLoggedIn: Boolean = false
)

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateEmail(email: String) {
        _uiState.value = _uiState.value.copy(
            email = email,
            isError = false,
            errorMessage = ""
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            isError = false,
            errorMessage = ""
        )
    }

    fun login() {
        val currentState = _uiState.value
        if (currentState.email.isEmpty() || currentState.password.isEmpty()) {
            _uiState.value = currentState.copy(
                isError = true,
                errorMessage = "Por favor complete todos los campos"
            )
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(currentState.email).matches()) {
            _uiState.value = currentState.copy(
                isError = true,
                errorMessage = "Por favor ingrese un email válido"
            )
            return
        }

        _uiState.value = currentState.copy(
            isLoading = true,
            isError = false,
            errorMessage = ""
        )

        // Curioso, se usa currentstate (estado actual) en vez de tener una forma de agarrar lo enviado (quizá si existe y no lo he visto XD)
        // Aparte Deprecated el Handler... pero funciona!
        android.os.Handler().postDelayed({
            val matched = UserRepository.users.find { u ->
                u.email.equals(currentState.email, ignoreCase = true) && u.password == currentState.password
            }

            if (matched != null) {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isLoggedIn = true
                )
            } else {
                _uiState.value = currentState.copy(
                    isLoading = false,
                    isError = true,
                    errorMessage = "Credenciales inválidas"
                )
            }
        }, 800)
    }

    fun resetError() {
        _uiState.value = _uiState.value.copy(
            isError = false,
            errorMessage = ""
        )
    }

    fun logout() {
        _uiState.value = LoginUiState()
    }
}