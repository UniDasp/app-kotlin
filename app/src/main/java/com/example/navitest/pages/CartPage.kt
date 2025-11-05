package com.example.navitest.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.navitest.model.CartRepository
import com.example.navitest.model.CartItem
import com.example.navitest.utils.toCLP
import com.example.navitest.viewmodel.LoginViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter

@Composable
fun CartPage(modifier: Modifier = Modifier) {
    val items = CartRepository.items
    val viewModel: LoginViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = modifier.padding(16.dp)) {
        Text("Carrito", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Tu carrito está vacío", style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
            }
            return@Column
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(items, key = { it.product.id }) { cartItem: CartItem ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = cartItem.product.image,
                            contentDescription = cartItem.product.name,
                            modifier = Modifier
                                .size(72.dp)
                                .clip(RoundedCornerShape(6.dp)),
                            contentScale = ContentScale.Crop,
                            placeholder = ColorPainter(Color(0xFFECEFF1)),
                            error = ColorPainter(Color(0xFFCFD8DC))
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(cartItem.product.name, style = MaterialTheme.typography.titleMedium, maxLines = 2)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(cartItem.product.price.toCLP(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("Subtotal: ${(cartItem.product.price * cartItem.quantity).toCLP()}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            var qty by remember(cartItem.product.id) { mutableStateOf(cartItem.quantity) }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = {
                                    val newQty = (qty - 1).coerceAtLeast(0)
                                    qty = newQty
                                    CartRepository.updateQuantity(cartItem.product, newQty)
                                }) {
                                        Text("-")
                                }

                                Text(qty.toString(), modifier = Modifier.padding(horizontal = 4.dp))

                                IconButton(onClick = {
                                    val newQty = qty + 1
                                    qty = newQty
                                    CartRepository.updateQuantity(cartItem.product, newQty)
                                }) {
                                        Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                }
                            }

                            IconButton(onClick = { CartRepository.remove(cartItem.product) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        val subtotal = items.sumOf { it.product.price * it.quantity }
        val shipping = if (subtotal > 0) 2990 else 0
        val total = subtotal + shipping

        Divider()
        Spacer(Modifier.height(8.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Subtotal", style = MaterialTheme.typography.bodyLarge)
                Text(subtotal.toCLP(), style = MaterialTheme.typography.bodyLarge)
            }
            Spacer(Modifier.height(4.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Envío", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(shipping.toCLP(), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Total", style = MaterialTheme.typography.titleLarge)
                Text(total.toCLP(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            }
        }

        Spacer(Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = { /* TODO: navigate back */ }, modifier = Modifier.weight(1f)) {
                Text("Seguir comprando")
            }

            Button(onClick = {
                CartRepository.clear()
            }, modifier = Modifier.weight(1f)) {
                Text(if (uiState.isLoggedIn) "Pagar" else "Pagar como invitado")
            }
        }

        if (!uiState.isLoggedIn) {
            TextButton(onClick = { /* TODO: open login */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Iniciar sesión para guardar tu pedido")
            }
        }
    }
}
