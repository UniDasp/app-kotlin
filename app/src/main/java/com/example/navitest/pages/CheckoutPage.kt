package com.example.navitest.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.navitest.api.PaymentRepository
import com.example.navitest.model.CartItem
import com.example.navitest.utils.toCLP
import com.example.navitest.viewmodel.AuthViewModel
import com.example.navitest.viewmodel.CartViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutPage(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToProducts: () -> Unit
) {
    val items by cartViewModel.items.collectAsState()
    val total by cartViewModel.total.collectAsState()
    val descuento by cartViewModel.descuentoEstudiante.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    var step by remember { mutableStateOf(1) }
    var processing by remember { mutableStateOf(false) }
    var orderNumber by remember { mutableStateOf("") }
    var paymentTotal by remember { mutableStateOf(0.0) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var region by remember { mutableStateOf("") }
    var metodoPago by remember { mutableStateOf("tarjeta") }
    var numeroTarjeta by remember { mutableStateOf("") }
    var nombreTarjeta by remember { mutableStateOf("") }
    var expiracion by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    
    val scope = rememberCoroutineScope()
    val paymentRepository = remember { PaymentRepository() }
    val esEstudiante = email.lowercase().endsWith("@duocuc.cl")
    val totalConDescuento = total - descuento
    
    
    LaunchedEffect(currentUser) {
        currentUser?.let { user ->
            nombre = user.fullName
            email = user.email
            telefono = user.telefono ?: ""
            direccion = user.direccion ?: ""
            ciudad = user.ciudad ?: ""
            region = user.region ?: ""
        }
    }
    
    
    if (items.isEmpty() && step < 3) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "🛒",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Agrega productos para continuar con tu compra.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = onNavigateToProducts) {
                        Text("Explorar Productos")
                    }
                }
            }
        }
        return
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        
        if (step == 3) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "¡Compra exitosa!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Tu pedido ha sido procesado correctamente. Recibirás un correo de confirmación en",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        email,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(Modifier.height(16.dp))
                    
                    if (esEstudiante && descuento > 0) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "🎓 Descuento estudiantil aplicado: Ahorraste ${descuento.toCLP()}",
                                modifier = Modifier.padding(12.dp),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Número de orden: #$orderNumber",
                            modifier = Modifier.padding(12.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    Spacer(Modifier.height(8.dp))
                    
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Total pagado: ${paymentTotal.toCLP()}",
                            modifier = Modifier.padding(12.dp),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    
                    Spacer(Modifier.height(24.dp))
                    
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onNavigateBack,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Volver al inicio")
                        }
                        OutlinedButton(
                            onClick = onNavigateToProducts,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Seguir comprando")
                        }
                    }
                }
            }
            return
        }
        
        
        Text(
            "💳 Finalizar Compra",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(Modifier.height(16.dp))
        
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StepIndicator(number = 1, label = "Datos", active = step >= 1)
            HorizontalDivider(modifier = Modifier.width(40.dp))
            StepIndicator(number = 2, label = "Pago", active = step >= 2)
            HorizontalDivider(modifier = Modifier.width(40.dp))
            StepIndicator(number = 3, label = "Confirmación", active = step >= 3)
        }
        
        Spacer(Modifier.height(24.dp))
        
        
        if (step < 3) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Resumen del pedido",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(Modifier.height(12.dp))
                    
                    items.forEach { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    item.name,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    "x${item.quantity}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Text(
                                (item.price * item.quantity).toCLP(),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                    
                    HorizontalDivider()
                    Spacer(Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(total.toCLP())
                    }
                    
                    val esEstudiante = currentUser?.email?.lowercase()?.endsWith("@duocuc.cl") == true
                    if (esEstudiante && descuento > 0) {
                        Spacer(Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "🎓 Descuento (20%)",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "-${descuento.toCLP()}",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Envío", color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text("Gratis", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                    
                    if (esEstudiante && descuento > 0) {
                        Spacer(Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Text(
                                "¡Descuento estudiantil aplicado! 🎉",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(thickness = 2.dp)
                    Spacer(Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text(
                            totalConDescuento.toCLP(),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
        }
        
        
        
        if (step == 1) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "📋 Datos de envío",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                            
                            Spacer(Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = nombre,
                                onValueChange = { nombre = it },
                                label = { Text("Nombre completo *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = telefono,
                                onValueChange = { telefono = it },
                                label = { Text("Teléfono *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = direccion,
                                onValueChange = { direccion = it },
                                label = { Text("Dirección *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(Modifier.height(12.dp))
                            
                            OutlinedTextField(
                                value = ciudad,
                                onValueChange = { ciudad = it },
                                label = { Text("Ciudad *") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            
                            Spacer(Modifier.height(12.dp))
                            
                            var expanded by remember { mutableStateOf(false) }
                            val regiones = listOf(
                                "",
                                "metropolitana",
                                "valparaiso",
                                "biobio",
                                "araucania",
                                "loslagos"
                            )
                            
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = region.replaceFirstChar { it.uppercase() },
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Región *") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    DropdownMenuItem(
                                        text = { Text("Selecciona una región") },
                                        onClick = {
                                            region = ""
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Metropolitana") },
                                        onClick = {
                                            region = "metropolitana"
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Valparaíso") },
                                        onClick = {
                                            region = "valparaiso"
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Biobío") },
                                        onClick = {
                                            region = "biobio"
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Araucanía") },
                                        onClick = {
                                            region = "araucania"
                                            expanded = false
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Los Lagos") },
                                        onClick = {
                                            region = "loslagos"
                                            expanded = false
                                        }
                                    )
                                }
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            
                            Button(
                                onClick = {
                                    if (nombre.isNotBlank() && email.isNotBlank() && telefono.isNotBlank() && 
                                        direccion.isNotBlank() && ciudad.isNotBlank() && region.isNotBlank()) {
                                        step = 2
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Continuar al pago →")
                            }
                }
            }
        }
        
        
        if (step == 2) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "💳 Método de pago",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                            
                            Spacer(Modifier.height(16.dp))
                            
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = metodoPago == "tarjeta",
                                    onClick = { metodoPago = "tarjeta" }
                                )
                                Text("💳 Tarjeta de crédito/débito")
                            }
                            
                            Spacer(Modifier.height(8.dp))
                            
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = metodoPago == "transferencia",
                                    onClick = { metodoPago = "transferencia" }
                                )
                                Text("🏦 Transferencia bancaria")
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            
                            
                            if (metodoPago == "tarjeta") {
                                OutlinedTextField(
                                    value = numeroTarjeta,
                                    onValueChange = { if (it.length <= 19) numeroTarjeta = it },
                                    label = { Text("Número de tarjeta *") },
                                    placeholder = { Text("1234 5678 9012 3456") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                Spacer(Modifier.height(12.dp))
                                
                                OutlinedTextField(
                                    value = nombreTarjeta,
                                    onValueChange = { nombreTarjeta = it },
                                    label = { Text("Nombre en la tarjeta *") },
                                    placeholder = { Text("JUAN PEREZ") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                
                                Spacer(Modifier.height(12.dp))
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = expiracion,
                                        onValueChange = { if (it.length <= 5) expiracion = it },
                                        label = { Text("Expiración *") },
                                        placeholder = { Text("MM/AA") },
                                        modifier = Modifier.weight(1f)
                                    )
                                    
                                    OutlinedTextField(
                                        value = cvv,
                                        onValueChange = { if (it.length <= 4) cvv = it },
                                        label = { Text("CVV *") },
                                        placeholder = { Text("123") },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            
                            
                            if (metodoPago == "transferencia") {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                            "Datos para transferencia:",
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(Modifier.height(8.dp))
                                        Text("Banco: Banco Estado")
                                        Text("Cuenta: 12345678")
                                        Text("RUT: 12.345.678-9")
                                    }
                                }
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            
                            Button(
                                onClick = {
                                    if (metodoPago == "transferencia" || 
                                        (numeroTarjeta.isNotBlank() && nombreTarjeta.isNotBlank() && 
                                         expiracion.isNotBlank() && cvv.isNotBlank())) {
                                        scope.launch {
                                            processing = true
                                            errorMessage = null
                                            
                                            try {
                                                val user = currentUser
                                                val token = authViewModel.getAuthToken()
                                                
                                                if (user == null || token.isNullOrEmpty()) {
                                                    
                                                    delay(1500)
                                                    orderNumber = System.currentTimeMillis().toString(36).uppercase()
                                                    paymentTotal = totalConDescuento
                                                    processing = false
                                                    step = 3
                                                    cartViewModel.clear()
                                                    return@launch
                                                }
                                                
                                                
                                                val initiateResult = paymentRepository.initiatePayment(
                                                    token = token,
                                                    userId = user.id,
                                                    userEmail = email,
                                                    totalAmount = totalConDescuento,
                                                    paymentMethod = metodoPago,
                                                    shippingAddress = direccion,
                                                    shippingCity = ciudad,
                                                    shippingPhone = telefono,
                                                    items = items
                                                )
                                                
                                                if (initiateResult.isFailure) {
                                                    throw initiateResult.exceptionOrNull() ?: Exception("Error al iniciar pago")
                                                }
                                                
                                                val paymentResponse = initiateResult.getOrNull()!!
                                                val paymentId = paymentResponse.id ?: paymentResponse.paymentId ?: ""
                                                orderNumber = paymentId
                                                paymentTotal = paymentResponse.totalAmount ?: totalConDescuento
                                                
                                                
                                                if (metodoPago == "tarjeta" && paymentId.isNotEmpty()) {
                                                    val parts = expiracion.split("/")
                                                    val mm = parts.getOrNull(0)?.trim() ?: ""
                                                    val yy = parts.getOrNull(1)?.trim() ?: ""
                                                    
                                                    val paymentToken = paymentResponse.paymentToken
                                                    val confirmResult = paymentRepository.confirmPayment(
                                                        token = paymentToken,
                                                        paymentId = paymentId,
                                                        cardNumber = numeroTarjeta,
                                                        cardholderName = nombreTarjeta,
                                                        expirationMonth = mm,
                                                        expirationYear = yy,
                                                        cvv = cvv
                                                    )
                                                    
                                                    if (confirmResult.isFailure) {
                                                        throw confirmResult.exceptionOrNull() ?: Exception("Error al confirmar pago")
                                                    }
                                                }
                                                
                                                processing = false
                                                step = 3
                                                cartViewModel.clear()
                                                
                                            } catch (e: Exception) {
                                                errorMessage = e.message ?: "Error al procesar el pago"
                                                processing = false
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !processing
                            ) {
                                if (processing) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(20.dp),
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text("Procesando pago...")
                                } else {
                                    Text("Confirmar y pagar ${totalConDescuento.toCLP()}")
                                }
                            }
                            
                            errorMessage?.let { error ->
                                Spacer(Modifier.height(8.dp))
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        error,
                                        modifier = Modifier.padding(12.dp),
                                        color = MaterialTheme.colorScheme.onErrorContainer
                                    )
                                }
                            }
                            
                            Spacer(Modifier.height(8.dp))
                            
                            OutlinedButton(
                                onClick = { step = 1 },
                                modifier = Modifier.fillMaxWidth(),
                                enabled = !processing
                            ) {
                            Text("← Volver")
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(number: Int, label: String, active: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    number.toString(),
                    color = if (active) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
