package com.example.navitest.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.navitest.api.AuthRepository
import com.example.navitest.api.NotificationRepository
import com.example.navitest.model.User
import com.example.navitest.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

sealed class AuthState {
    object Initial : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    object NotAuthenticated : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val authRepository = AuthRepository()
    private val notificationRepository = NotificationRepository()
    private val preferencesManager = PreferencesManager(application)
    private val context = application.applicationContext

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    
    private val _shouldReloadCart = MutableStateFlow(0)
    val shouldReloadCart: StateFlow<Int> = _shouldReloadCart.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val token = preferencesManager.authToken.first()
            if (!token.isNullOrEmpty()) {
                
                val result = authRepository.getCurrentUser(token)
                if (result.isSuccess) {
                    _currentUser.value = result.getOrNull()
                    _authState.value = AuthState.Authenticated
                } else {
                    
                    preferencesManager.logout()
                    _authState.value = AuthState.NotAuthenticated
                }
            } else {
                _authState.value = AuthState.NotAuthenticated
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            _authState.value = AuthState.Loading

            val result = authRepository.login(username, password)
            
            if (result.isSuccess) {
                val loginResponse = result.getOrNull()
                if (loginResponse != null && loginResponse.token.isNotEmpty()) {
                    
                    preferencesManager.saveAuthToken(loginResponse.token)
                    preferencesManager.setLoggedIn(true)
                    
                    
                    if (loginResponse.user != null) {
                        preferencesManager.saveUserEmail(loginResponse.user.email)
                        preferencesManager.saveUserName("${loginResponse.user.nombre} ${loginResponse.user.apellido}")
                        preferencesManager.saveUserId(loginResponse.user.id.toString())
                        _currentUser.value = loginResponse.user
                    } else {
                        
                        val userResult = authRepository.getCurrentUser(loginResponse.token)
                        if (userResult.isSuccess) {
                            val user = userResult.getOrNull()
                            if (user != null) {
                                preferencesManager.saveUserEmail(user.email)
                                preferencesManager.saveUserName("${user.nombre} ${user.apellido}")
                                preferencesManager.saveUserId(user.id.toString())
                                _currentUser.value = user
                            }
                        }
                    }
                    
                    _authState.value = AuthState.Authenticated
                    _shouldReloadCart.value++ 
                    
                    
                    registerFCMToken(loginResponse.token)
                } else {
                    errorMessage = "No se recibió token de autenticación"
                    _authState.value = AuthState.Error(errorMessage!!)
                }
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                _authState.value = AuthState.Error(errorMessage!!)
            }
            
            isLoading = false
        }
    }

    fun register(
        username: String,
        password: String,
        email: String,
        firstName: String,
        lastName: String,
        phone: String? = null,
        address: String? = null,
        region: String? = null,
        onResult: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            _authState.value = AuthState.Loading

            val result = authRepository.register(
                username = username,
                password = password,
                email = email,
                firstName = firstName,
                lastName = lastName,
                phone = phone,
                address = address,
                region = region
            )
            
            if (result.isSuccess) {
                val registerResponse = result.getOrNull()
                if (registerResponse != null) {
                    
                    isLoading = false
                    _authState.value = AuthState.NotAuthenticated
                    onResult(true)
                } else {
                    isLoading = false
                    _authState.value = AuthState.NotAuthenticated
                    onResult(false)
                }
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error al registrarse"
                _authState.value = AuthState.Error(errorMessage!!)
                isLoading = false
                onResult(false)
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            
            val token = preferencesManager.authToken.first()
            if (!token.isNullOrEmpty()) {
                try {
                    notificationRepository.removeDevice(token, "ANDROID")
                    Log.d("AuthViewModel", "FCM token removed successfully")
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error removing FCM token", e)
                }
            }
            
            preferencesManager.logout()
            _currentUser.value = null
            _authState.value = AuthState.NotAuthenticated
            _shouldReloadCart.value++ 
        }
    }

    fun clearError() {
        errorMessage = null
    }

    fun updateProfile(
        nombre: String? = null,
        apellido: String? = null,
        telefono: String? = null,
        direccion: String? = null,
        region: String? = null,
        ciudad: String? = null
    ) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            val token = preferencesManager.authToken.first()
            if (token.isNullOrEmpty()) {
                errorMessage = "No hay sesión activa"
                isLoading = false
                return@launch
            }

            val currentU = _currentUser.value
            if (currentU == null) {
                errorMessage = "No hay usuario cargado"
                isLoading = false
                return@launch
            }

            
            val updatedUser = currentU.copy(
                nombre = nombre ?: currentU.nombre,
                apellido = apellido ?: currentU.apellido,
                telefono = telefono ?: currentU.telefono,
                direccion = direccion ?: currentU.direccion,
                region = region ?: currentU.region,
                ciudad = ciudad ?: currentU.ciudad
            )

            val result = authRepository.updateProfile(token, currentU.id, updatedUser)

            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    _currentUser.value = user
                    preferencesManager.saveUserName(user.fullName)
                    preferencesManager.saveUserEmail(user.email)
                }
            } else {
                errorMessage = result.exceptionOrNull()?.message ?: "Error al actualizar perfil"
            }

            isLoading = false
        }
    }
    
    suspend fun getAuthToken(): String? {
        return preferencesManager.authToken.first()
    }
    
    private fun registerFCMToken(authToken: String) {
        viewModelScope.launch {
            try {
                
                val userId = preferencesManager.getUserId()
                
                if (userId == null) {
                    Log.w("AuthViewModel", "⚠️ No userId available, fetching from server...")
                    
                    val userResult = authRepository.getCurrentUser(authToken)
                    if (userResult.isSuccess) {
                        val user = userResult.getOrNull()
                        if (user != null) {
                            preferencesManager.saveUserId(user.id.toString())
                            Log.d("AuthViewModel", "✅ UserId fetched and saved: ${user.id}")
                            
                            registerFCMToken(authToken)
                            return@launch
                        }
                    }
                    Log.e("AuthViewModel", "❌ Could not obtain userId")
                    return@launch
                }
                
                val sharedPreferences = context.getSharedPreferences("levelup_prefs", Context.MODE_PRIVATE)
                val fcmToken = sharedPreferences.getString("fcm_token", null)
                
                Log.d("AuthViewModel", "=== FCM Token Registration ===")
                Log.d("AuthViewModel", "User ID: $userId")
                
                
                try {
                    val tokenParts = authToken.replace("Bearer ", "").split(".")
                    if (tokenParts.size >= 2) {
                        val payload = String(android.util.Base64.decode(tokenParts[1], android.util.Base64.URL_SAFE))
                        Log.d("AuthViewModel", "JWT Payload: $payload")
                        Log.w("AuthViewModel", "⚠️ PROBLEMA: El JWT tiene 'sub' con username en lugar de userId")
                        Log.w("AuthViewModel", "⚠️ El backend necesita que 'sub' contenga: $userId")
                    }
                } catch (e: Exception) {
                    Log.e("AuthViewModel", "Error decoding JWT", e)
                }
                
                Log.d("AuthViewModel", "FCM Token: $fcmToken")
                
                if (fcmToken != null) {
                    Log.d("AuthViewModel", "Attempting to register FCM token...")
                    val result = notificationRepository.registerDevice(authToken, fcmToken, "ANDROID")
                    
                    result.fold(
                        onSuccess = {
                            Log.d("AuthViewModel", "✅ FCM token registered successfully")
                        },
                        onFailure = { error ->
                            Log.e("AuthViewModel", "❌ Failed to register FCM token: ${error.message}")
                            Log.e("AuthViewModel", "❌ CAUSA: El JWT contiene username 'Diego' en 'sub', pero el backend espera userId: $userId")
                            Log.e("AuthViewModel", "❌ SOLUCIÓN: El backend debe generar JWTs con userId en el campo 'sub'")
                        }
                    )
                } else {
                    Log.w("AuthViewModel", "⚠️ No FCM token available to register")
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "❌ Exception while registering FCM token", e)
            }
        }
    }
}
