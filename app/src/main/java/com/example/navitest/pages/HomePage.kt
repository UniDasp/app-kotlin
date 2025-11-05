
package com.example.navitest.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import com.example.navitest.model.RecentRepository
import com.example.navitest.model.ProductRepository
import com.example.navitest.model.CartRepository
import com.example.navitest.utils.toCLP
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier) {
    var showProductDialog by remember { mutableStateOf<com.example.navitest.model.Product?>(null) }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(13.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Destacado",
                        modifier = Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Column {
                        Text(
                            "¡Oferta Especial!",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            "Usando tu correo de DuocUC obtén un 20% de descuento",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            maxLines = 2
                        )
                    }
                }
            }
        }

    if (showProductDialog != null) {
        AlertDialog(
            onDismissRequest = { showProductDialog = null },
            properties = DialogProperties(usePlatformDefaultWidth = false),
            title = null,
            text = {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .widthIn(max = 640.dp)
                                    .padding(8.dp)
                            ) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    shape = MaterialTheme.shapes.medium
                                ) {
                                    AsyncImage(
                                        model = showProductDialog!!.image,
                                        contentDescription = showProductDialog!!.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop,
                                        placeholder = ColorPainter(Color(0xFFE0E0E0)),
                                        error = ColorPainter(Color(0xFFBDBDBD))
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = showProductDialog!!.name,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = showProductDialog!!.price.toCLP(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Text(
                                    "Descripción",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )

                                Text(
                                    showProductDialog!!.description,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
            },
            confirmButton = {
                FilledTonalButton(onClick = { showProductDialog = null }, modifier = Modifier.fillMaxWidth()) {
                    Text("Cerrar")
                }
            },
            dismissButton = {
                FilledTonalButton(onClick = {
                    showProductDialog?.let { CartRepository.add(it) }
                    showProductDialog = null
                }) {
                    Text("Agregar al carrito")
                }
            }
        )
    }

        // Vistos recientemente (solo aparece si hay items en RecentRepository) xd
        val recent = RecentRepository.items
        if (recent.isNotEmpty()) {
            Text(
                "Vistos recientemente",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(recent) { p ->
                    ListItem(
                        headlineContent = { Text(p.name) },
                        supportingContent = { Text(p.price.toCLP()) },
                        leadingContent = {
                            AsyncImage(
                                model = p.image,
                                contentDescription = p.name,
                                modifier = Modifier
                                    .size(56.dp)
                                    .clip(MaterialTheme.shapes.small),
                                contentScale = ContentScale.Crop,
                                placeholder = ColorPainter(Color(0xFFECEFF1)),
                                error = ColorPainter(Color(0xFFCFD8DC))
                            )
                        },
                        modifier = Modifier.clickable {

                            showProductDialog = p
                            RecentRepository.add(p)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
        Text(
            "Categorías Populares",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val categories = listOf(
            Triple("Consolas","Consolas","https://i.imgur.com/Ceu19Yq.jpeg"),
            Triple("Accesorios","Accesorios","https://i.imgur.com/BdFEBnx.jpeg"),
            Triple("Computadores Gamers","Computadores Gamers","https://i.imgur.com/4Szrfso.png"),
            Triple("Juegos de Mesa","Juegos de Mesa","https://i.imgur.com/VrC741p.jpeg")
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(categories) { cat ->
                ElevatedCard(
                    modifier = Modifier.width(160.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = cat.third,
                            contentDescription = cat.first,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(MaterialTheme.shapes.small),
                            contentScale = ContentScale.Crop,
                            placeholder = ColorPainter(Color(0xFFF5F5F5)),
                            error = ColorPainter(Color(0xFFEEEEEE))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = cat.second,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val featuredProducts = remember { ProductRepository.products.filter { it.featured } }
        if (featuredProducts.isNotEmpty()) {
            Text(
                "Destacados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(featuredProducts) { p ->
                    ElevatedCard(
                        modifier = Modifier
                            .width(200.dp)
                            .clickable {
                                showProductDialog = p
                                RecentRepository.add(p)
                            }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            AsyncImage(
                                model = p.image,
                                contentDescription = p.name,
                                modifier = Modifier
                                    .height(110.dp)
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.small),
                                contentScale = ContentScale.Crop,
                                placeholder = ColorPainter(Color(0xFFF5F5F5)),
                                error = ColorPainter(Color(0xFFEEEEEE))
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(p.name, style = MaterialTheme.typography.titleMedium, maxLines = 1)
                            Text(p.price.toCLP(), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}