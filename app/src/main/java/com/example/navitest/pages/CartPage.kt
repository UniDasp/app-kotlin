package com.example.navitest.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.navitest.model.CartItem
import com.example.navitest.utils.toCLP
import com.example.navitest.viewmodel.CartViewModel

@Composable
fun CartPage(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel,
    authViewModel: com.example.navitest.viewmodel.AuthViewModel,
    onNavigateToCheckout: () -> Unit = {}
) {
    val items by cartViewModel.items.collectAsState()
    val total by cartViewModel.total.collectAsState()
    val dealSavings by cartViewModel.dealSavings.collectAsState()
    val descuentoEstudiante by cartViewModel.descuentoEstudiante.collectAsState()
    val totalConDescuento by cartViewModel.totalConDescuento.collectAsState()
    val loading by cartViewModel.loading.collectAsState()
    val error by cartViewModel.error.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()
    
    val esEstudiante = currentUser?.email?.lowercase()?.endsWith("@duocuc.cl") == true

    LaunchedEffect(Unit) {
        cartViewModel.loadCart()
    }

    Column(modifier = modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Carrito", style = MaterialTheme.typography.headlineMedium)
            
            if (items.isNotEmpty()) {
                TextButton(onClick = { cartViewModel.clear() }) {
                    Text("Vaciar carrito")
                }
            }
        }
        
        Spacer(Modifier.height(12.dp))

        error?.let { errorMsg ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMsg,
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    IconButton(onClick = { cartViewModel.clearError() }) {
                        Text("✕")
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        if (loading && items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Column
        }

        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Tu carrito está vacío",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "¡Agrega productos para empezar!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            return@Column
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(items, key = { it.itemId }) { cartItem ->
                CartItemCard(
                    cartItem = cartItem,
                    onUpdateQuantity = { newQty ->
                        cartViewModel.updateQuantity(cartItem.itemId, newQty)
                    },
                    onRemove = {
                        cartViewModel.removeItem(cartItem.itemId)
                    },
                    loading = loading
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        
        val subtotal = items.sumOf { (it.originalPrice ?: it.price) * it.quantity }
        
        val dealSavings = items.sumOf {
            if (it.originalPrice != null && it.discountedPrice != null)
                (it.originalPrice - it.discountedPrice) * it.quantity
            else 0.0
        }
        val descuento = descuentoEstudiante
        val shipping = 0.0
        val finalTotal = subtotal - dealSavings - descuento + shipping

        
        if (esEstudiante && descuento > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("🎓", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            "Descuento Estudiantil DuocUC",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Ahorro del 20%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        HorizontalDivider()
        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", style = MaterialTheme.typography.bodyLarge)
                Text(subtotal.toCLP(), style = MaterialTheme.typography.bodyLarge)
            }
            if (dealSavings > 0) {
                Spacer(Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "🎁 Descuentos en productos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4CAF50)
                    )
                    Text(
                        "- ${dealSavings.toCLP()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            if (esEstudiante && descuento > 0) {
                Spacer(Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        "Descuento estudiantil (20%)",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        "- ${descuento.toCLP()}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Envío",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "GRATIS",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.titleLarge)
                Text(
                    finalTotal.toCLP(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onNavigateToCheckout,
            modifier = Modifier.fillMaxWidth(),
            enabled = !loading && items.isNotEmpty()
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.width(8.dp))
            }
            Text("Procesar Pago (${finalTotal.toCLP()})")
        }
    }
}

@Composable
private fun CartItemCard(
    cartItem: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit,
    loading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = cartItem.image,
                contentDescription = cartItem.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(6.dp)),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color(0xFFECEFF1)),
                error = ColorPainter(Color(0xFFCFD8DC))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        cartItem.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (cartItem.discountPercentage != null && cartItem.discountPercentage > 0) {
                        Surface(
                            color = Color.Red,
                            shape = MaterialTheme.shapes.extraSmall
                        ) {
                            Text(
                                "-${cartItem.discountPercentage.toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                if (cartItem.originalPrice != null && cartItem.discountedPrice != null && cartItem.originalPrice > cartItem.discountedPrice) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            cartItem.originalPrice.toCLP(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                        Text(
                            cartItem.discountedPrice.toCLP(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Text(
                        (cartItem.discountedPrice ?: cartItem.price).toCLP(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    "Subtotal: ${((cartItem.discountedPrice ?: cartItem.price) * cartItem.quantity.toDouble()).toCLP()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (cartItem.originalPrice != null && cartItem.discountedPrice != null && cartItem.originalPrice > cartItem.discountedPrice) {
                    Text(
                        "Ahorras: ${(cartItem.originalPrice - cartItem.discountedPrice).toCLP()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Bold
                    )
                }
                if (!cartItem.activeDealName.isNullOrBlank()) {
                    Text(
                        "Promo: ${cartItem.activeDealName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF1976D2)
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "Stock: ${cartItem.stock}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (cartItem.stock < 5) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    IconButton(
                        onClick = {
                            val newQty = (cartItem.quantity - 1).coerceAtLeast(1)
                            onUpdateQuantity(newQty)
                        },
                        enabled = !loading && cartItem.quantity > 1
                    ) {
                        Text("-", style = MaterialTheme.typography.titleLarge)
                    }

                    Text(
                        cartItem.quantity.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.widthIn(min = 24.dp)
                    )

                    IconButton(
                        onClick = {
                            val newQty = (cartItem.quantity + 1).coerceAtMost(cartItem.stock)
                            onUpdateQuantity(newQty)
                        },
                        enabled = !loading && cartItem.quantity < cartItem.stock
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aumentar", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                IconButton(
                    onClick = onRemove,
                    enabled = !loading
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}