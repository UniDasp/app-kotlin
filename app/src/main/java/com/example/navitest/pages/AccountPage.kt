package com.example.navitest.pages

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import com.example.navitest.utils.BiometricAuthManager
import com.example.navitest.utils.PreferencesManager
import com.example.navitest.viewmodel.AuthViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountPage(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel,
    preferencesManager: PreferencesManager,
    biometricAuthManager: BiometricAuthManager,
    onNavigateToPayments: () -> Unit = {}
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val isLoading by remember { derivedStateOf { authViewModel.isLoading } }
    val errorMessage by remember { derivedStateOf { authViewModel.errorMessage } }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var isBiometricEnabled by remember { mutableStateOf(false) }
    var areNotificationsEnabled by remember { mutableStateOf(true) }
    
    
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    
    var showSuccessMessage by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        scope.launch {
            isBiometricEnabled = preferencesManager.isBiometricEnabled.first()
            areNotificationsEnabled = preferencesManager.notificationsEnabled.first()
        }
    }

    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            nombre = user.nombre ?: ""
            apellido = user.apellido ?: ""
            telefono = user.telefono ?: ""
            direccion = user.direccion ?: ""
            region = user.region ?: ""
            ciudad = user.ciudad ?: ""
        }
    }

    
    if (currentUser == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(16.dp))
                Text("Cargando información del usuario...")
            }
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = currentUser?.fullName ?: "Usuario",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = currentUser?.email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            
            if (showSuccessMessage) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.CheckCircle, null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.width(8.dp))
                        Text("Perfil actualizado correctamente", modifier = Modifier.weight(1f))
                        IconButton(onClick = { showSuccessMessage = false }) {
                            Text("✕")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            
            errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, null, tint = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.width(8.dp))
                        Text(error, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onErrorContainer)
                        IconButton(onClick = { authViewModel.clearError() }) {
                            Text("✕")
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
            }

            
            Text(
                "Información Personal",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = apellido,
                        onValueChange = { apellido = it },
                        label = { Text("Apellido") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = telefono,
                        onValueChange = { telefono = it },
                        label = { Text("Teléfono") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Phone, null) }
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = direccion,
                        onValueChange = { direccion = it },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.Home, null) }
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    
                    var expandedRegion by remember { mutableStateOf(false) }
                    val regiones = listOf(
                        "", "Región Metropolitana", "Valparaíso", "Biobío", "Maule",
                        "La Araucanía", "Los Lagos", "O'Higgins", "Coquimbo", "Antofagasta",
                        "Tarapacá", "Atacama", "Aysén", "Magallanes", "Arica y Parinacota", "Ñuble", "Los Ríos"
                    )
                    
                    ExposedDropdownMenuBox(
                        expanded = expandedRegion,
                        onExpandedChange = { expandedRegion = it }
                    ) {
                        OutlinedTextField(
                            value = region.ifEmpty { "Selecciona una región" },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Región") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedRegion) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )
                        
                        ExposedDropdownMenu(
                            expanded = expandedRegion,
                            onDismissRequest = { expandedRegion = false }
                        ) {
                            regiones.forEach { reg ->
                                DropdownMenuItem(
                                    text = { Text(reg.ifEmpty { "Selecciona una región" }) },
                                    onClick = {
                                        region = reg
                                        expandedRegion = false
                                    }
                                )
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(12.dp))
                    
                    OutlinedTextField(
                        value = ciudad,
                        onValueChange = { ciudad = it },
                        label = { Text("Ciudad") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Default.LocationOn, null) }
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            showSuccessMessage = false
                            authViewModel.updateProfile(
                                nombre = nombre,
                                apellido = apellido,
                                telefono = telefono.ifEmpty { null },
                                direccion = direccion.ifEmpty { null },
                                region = region.ifEmpty { null },
                                ciudad = ciudad.ifEmpty { null }
                            )
                            scope.launch {
                                kotlinx.coroutines.delay(500)
                                if (authViewModel.errorMessage == null) {
                                    showSuccessMessage = true
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && nombre.isNotBlank() && apellido.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(Modifier.width(8.dp))
                        }
                        Text("Guardar Cambios")
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            Spacer(Modifier.height(24.dp))

            
            Text(
                "Preferencias",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    
                    OutlinedButton(
                        onClick = onNavigateToPayments,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ShoppingCart, null)
                        Spacer(Modifier.width(8.dp))
                        Text("Ver Mis Pagos")
                    }
                    
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider()
                    Spacer(Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Autenticación Biométrica",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                if (biometricAuthManager.isBiometricAvailable()) 
                                    "Usa huella digital para desbloquear" 
                                else 
                                    "No disponible",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isBiometricEnabled,
                            enabled = biometricAuthManager.isBiometricAvailable(),
                            onCheckedChange = { enabled ->
                                if (enabled) {
                                    biometricAuthManager.authenticate(
                                        activity = context as FragmentActivity,
                                        title = "Configurar Autenticación",
                                        subtitle = "Verifica tu identidad",
                                        onSuccess = {
                                            scope.launch {
                                                preferencesManager.setBiometricEnabled(true)
                                                isBiometricEnabled = true
                                                Toast.makeText(context, "Activada", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        onError = { Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show() },
                                        onFailed = { Toast.makeText(context, "Fallida", Toast.LENGTH_SHORT).show() }
                                    )
                                } else {
                                    scope.launch {
                                        preferencesManager.setBiometricEnabled(false)
                                        isBiometricEnabled = false
                                    }
                                }
                            }
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Notificaciones",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "Recibe alertas sobre tus compras",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = areNotificationsEnabled,
                            onCheckedChange = { enabled ->
                                scope.launch {
                                    preferencesManager.setNotificationsEnabled(enabled)
                                    areNotificationsEnabled = enabled
                                }
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            
            OutlinedButton(
                onClick = {
                    authViewModel.logout()
                    Toast.makeText(context, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}
