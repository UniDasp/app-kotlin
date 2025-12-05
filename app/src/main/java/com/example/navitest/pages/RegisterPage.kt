package com.example.navitest.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.navitest.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

data class RegisterFormState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val email: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val address: String = "",
    val region: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPage(
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier,
    onNavigateToLogin: () -> Unit = {},
    onRegisterSuccess: () -> Unit = {}
) {
    var form by remember { mutableStateOf(RegisterFormState()) }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            
            Text(
                text = "🎮",
                style = MaterialTheme.typography.displayMedium
            )
            
            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "Regístrate para comenzar a comprar en LEVEL-UP GAMER",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(Modifier.height(24.dp))
            
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = form.firstName,
                    onValueChange = { form = form.copy(firstName = it) },
                    label = { Text("Nombre") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
                
                OutlinedTextField(
                    value = form.lastName,
                    onValueChange = { form = form.copy(lastName = it) },
                    label = { Text("Apellido") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            
            OutlinedTextField(
                value = form.username,
                onValueChange = { form = form.copy(username = it) },
                label = { Text("Usuario") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }
            )
            
            Spacer(Modifier.height(12.dp))
            
            
            OutlinedTextField(
                value = form.email,
                onValueChange = { form = form.copy(email = it) },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                supportingText = {
                    Text(
                        "🎓 Usa tu correo institucional para descuentos",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
            
            Spacer(Modifier.height(12.dp))
            
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = form.password,
                    onValueChange = { form = form.copy(password = it) },
                    label = { Text("Contraseña") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Lock else Icons.Default.Lock,
                                contentDescription = if (passwordVisible) "Ocultar" else "Mostrar"
                            )
                        }
                    }
                )
                
                OutlinedTextField(
                    value = form.confirmPassword,
                    onValueChange = { form = form.copy(confirmPassword = it) },
                    label = { Text("Confirmar") },
                    singleLine = true,
                    modifier = Modifier.weight(1f),
                    enabled = !isLoading,
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                if (confirmPasswordVisible) Icons.Default.Lock else Icons.Default.Lock,
                                contentDescription = if (confirmPasswordVisible) "Ocultar" else "Mostrar"
                            )
                        }
                    }
                )
            }
            
            Spacer(Modifier.height(12.dp))
            
            
            OutlinedTextField(
                value = form.phone,
                onValueChange = { form = form.copy(phone = it) },
                label = { Text("Teléfono") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                placeholder = { Text("+56 9 1234 5678") }
            )
            
            Spacer(Modifier.height(12.dp))
            
            
            OutlinedTextField(
                value = form.address,
                onValueChange = { form = form.copy(address = it) },
                label = { Text("Dirección") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                leadingIcon = { Icon(Icons.Default.Home, contentDescription = null) }
            )
            
            Spacer(Modifier.height(12.dp))
            
            
            OutlinedTextField(
                value = form.region,
                onValueChange = { form = form.copy(region = it) },
                label = { Text("Región") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )
            
            if (error != null) {
                Spacer(Modifier.height(12.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error!!,
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            Button(
                onClick = {
                    error = null
                    when {
                        form.username.isBlank() || form.password.isBlank() -> {
                            error = "Usuario y contraseña son requeridos"
                        }
                        form.password != form.confirmPassword -> {
                            error = "Las contraseñas no coinciden"
                        }
                        else -> {
                            isLoading = true
                            authViewModel.register(
                                username = form.username,
                                password = form.password,
                                email = form.email,
                                firstName = form.firstName,
                                lastName = form.lastName,
                                phone = form.phone,
                                address = form.address,
                                region = form.region
                            ) { success ->
                                isLoading = false
                                if (success) {
                                    
                                    onNavigateToLogin()
                                } else {
                                    error = authViewModel.errorMessage ?: "Error al registrar usuario"
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text("Crear Cuenta")
            }
            
            Spacer(Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            
            Text(
                text = "¿Ya tienes cuenta?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(Modifier.height(8.dp))
            
            OutlinedButton(
                onClick = onNavigateToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }
            
            Spacer(Modifier.height(24.dp))
        }
    }
}
