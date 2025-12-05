
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
import com.example.navitest.utils.toCLP
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.navitest.viewmodel.ProductsViewModel
import com.example.navitest.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel
) {
    var showProductDialog by remember { mutableStateOf<com.example.navitest.model.Product?>(null) }
    val featuredProducts by productsViewModel.featuredProducts.collectAsState()
    val allProducts by productsViewModel.allProducts.collectAsState()
    val isLoading = productsViewModel.isLoading
    val errorMessage = productsViewModel.errorMessage
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = "Bienvenido a Level Up",
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

        Spacer(modifier = Modifier.height(16.dp))

        
        com.example.navitest.components.DealsCarousel(
            onDealClick = { deal ->
                
                when (deal.type) {
                    com.example.navitest.model.DealType.ALL -> {
                        
                        productsViewModel.loadProducts()
                    }
                    com.example.navitest.model.DealType.CATEGORY -> {
                        
                    }
                    com.example.navitest.model.DealType.SPECIFIC_PRODUCT -> {
                        
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

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

                                Spacer(modifier = Modifier.height(8.dp))

                                
                                com.example.navitest.components.ProductPrice(
                                    product = showProductDialog!!,
                                    showDealName = true
                                )

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
                    showProductDialog?.let { cartViewModel.addItem(it) }
                    showProductDialog = null
                }) {
                    Text("Agregar al carrito")
                }
            }
        )
    }

        
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        
        if (errorMessage != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        
        if (!isLoading && featuredProducts.isNotEmpty()) {
            Text(
                "Productos Destacados",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(featuredProducts) { p ->
                    com.example.navitest.components.ProductCard(
                        product = p,
                        onClick = { showProductDialog = p }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
        
        
        if (!isLoading && allProducts.isNotEmpty()) {
            Text(
                "Todos los Productos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            items(allProducts.take(10)) { p ->
                com.example.navitest.components.ProductCard(
                    product = p,
                    onClick = { showProductDialog = p }
                )
            }
        }
        }

        
        if (!isLoading && allProducts.isEmpty() && errorMessage == null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No hay productos disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Vuelve más tarde para ver nuevos productos",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}