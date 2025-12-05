
package com.example.navitest.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.window.DialogProperties
import androidx.compose.material3.FilterChip
import coil.compose.AsyncImage
import com.example.navitest.utils.toCLP
import com.example.navitest.viewmodel.ProductsViewModel
import com.example.navitest.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(
    modifier: Modifier = Modifier,
    productsViewModel: ProductsViewModel,
    cartViewModel: CartViewModel
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf<Int?>(null) }
    var showProductDialog by remember { mutableStateOf<com.example.navitest.model.Product?>(null) }

    val categories by productsViewModel.categories.collectAsState()
    val allProducts by productsViewModel.allProducts.collectAsState()
    val isLoading = productsViewModel.isLoading
    val errorMessage = productsViewModel.errorMessage

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotBlank()) {
            productsViewModel.searchProducts(searchQuery)
        } else if (selectedCategoryId == null) {
            productsViewModel.loadProducts()
        }
    }

    LaunchedEffect(selectedCategoryId) {
        if (selectedCategoryId != null) {
            productsViewModel.filterByCategoryId(selectedCategoryId)
        } else if (searchQuery.isBlank()) {
            productsViewModel.loadProducts()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onSearch = { productsViewModel.searchProducts(searchQuery) },
            active = false,
            onActiveChange = { },
            placeholder = { Text("Buscar productos...") },
            leadingIcon = { Icon(Icons.Default.Search, "Buscar") },
            modifier = Modifier.fillMaxWidth()
        ) { }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                FilterChip(
                    selected = selectedCategoryId == category.id,
                    onClick = {
                        selectedCategoryId = if (selectedCategoryId == category.id) null else category.id
                    },
                    label = {
                        Text(
                            text = category.nombre,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(text = errorMessage, modifier = Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onErrorContainer)
            }
        } else if (allProducts.isEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(56.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("No se encontraron productos", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Intenta otra búsqueda o vuelve más tarde.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allProducts) { product ->
                    com.example.navitest.components.ProductCard(
                        product = product,
                        onClick = { showProductDialog = product }
                    )
                }
            }
        }

        if (showProductDialog != null) {
            AlertDialog(
                onDismissRequest = { showProductDialog = null },
                properties = DialogProperties(usePlatformDefaultWidth = false),
                modifier = Modifier.padding(16.dp),
                title = null,
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth()
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
                                contentScale = ContentScale.Crop
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
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
                            com.example.navitest.components.ProductPrice(
                                product = showProductDialog!!,
                                showDealName = true
                            )
                            
                            if (showProductDialog!!.featured) {
                                Surface(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text(
                                        "Destacado",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        
                        ListItem(
                            headlineContent = { Text(showProductDialog!!.categoryName) },
                            leadingContent = {
                                Surface(
                                    modifier = Modifier.size(32.dp),
                                    shape = MaterialTheme.shapes.small,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
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
                    FilledTonalButton(
                        onClick = { showProductDialog = null },
                        modifier = Modifier.fillMaxWidth()
                    ) {
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
    }
}
