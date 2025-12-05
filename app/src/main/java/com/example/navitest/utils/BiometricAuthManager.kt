package com.example.navitest.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class BiometricAuthManager(private val context: Context) {

    fun canAuthenticate(): Int {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )
    }

    fun isBiometricAvailable(): Boolean {
        return canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun authenticate(
        activity: FragmentActivity,
        title: String = "Autenticación Biométrica",
        subtitle: String = "Verifica tu identidad",
        description: String = "Usa tu huella digital o patrón para continuar",
        onSuccess: () -> Unit,
        onError: (String) -> Unit,
        onFailed: () -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        val biometricPrompt = BiometricPrompt(
            activity,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errString.toString())
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    companion object {
        fun getBiometricStatusMessage(status: Int): String {
            return when (status) {
                BiometricManager.BIOMETRIC_SUCCESS -> "Biométrico disponible"
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "No hay hardware biométrico disponible"
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Hardware biométrico no disponible actualmente"
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "No hay credenciales biométricas registradas"
                BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "Se requiere actualización de seguridad"
                BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> "No soportado"
                BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> "Estado desconocido"
                else -> "Error desconocido"
            }
        }
    }
}
