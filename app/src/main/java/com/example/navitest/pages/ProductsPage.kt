
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
import com.example.navitest.model.ProductRepository
import com.example.navitest.model.ProductCategory
import com.example.navitest.model.RecentRepository
import com.example.navitest.utils.toCLP

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPage(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<ProductCategory?>(null) }
    var showProductDialog by remember { mutableStateOf<com.example.navitest.model.Product?>(null) }

    val filters = ProductCategory.values().toList()
    val products = remember { ProductRepository.products }
    
    var selectedFeaturedProductIndex by remember { mutableStateOf(0) }
    val featuredProducts = remember { products.filter { it.featured } }
    
    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(5000)
            selectedFeaturedProductIndex = (selectedFeaturedProductIndex + 1) % featuredProducts.size
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
            onSearch = { },
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
            filters.forEach { category ->
                FilterChip(
                    selected = selectedFilter == category,
                    onClick = {
                        selectedFilter = if (selectedFilter == category) null else category
                    },
                    label = {
                        Text(
                            text = category.displayName,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    },
                    shape = when (filters.indexOf(category)) {
                        0 -> MaterialTheme.shapes.extraLarge.copy(
                            topEnd = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                        filters.lastIndex -> MaterialTheme.shapes.extraLarge.copy(
                            topStart = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp)
                        )
                        else -> MaterialTheme.shapes.extraLarge.copy(
                            topStart = CornerSize(0.dp),
                            bottomStart = CornerSize(0.dp),
                            topEnd = CornerSize(0.dp),
                            bottomEnd = CornerSize(0.dp)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val filteredProducts = products.filter {
                (selectedFilter == null || it.category == selectedFilter) &&
                (it.name.contains(searchQuery, ignoreCase = true) || searchQuery.isEmpty())
            }
            
            items(filteredProducts) { product ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showProductDialog = product; RecentRepository.add(product) },
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    model = product.image,
                                    contentDescription = product.name,
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                if (product.featured) {
                                    Surface(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .align(Alignment.TopEnd),
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
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            text = product.price.toCLP(),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Text(
                            text = product.category.displayName,
                            style = MaterialTheme.typography.bodySmall,
                            color = product.category.color,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                                FilledTonalButton(
                                    onClick = { showProductDialog = product; RecentRepository.add(product) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                ) {
                                    Text("Ver detalles")
                                }
                    }
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
                            Text(
                                text = showProductDialog!!.price.toCLP(),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.primary
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
                            headlineContent = { Text(showProductDialog!!.category.displayName) },
                            leadingContent = { 
                                Surface(
                                    modifier = Modifier.size(32.dp),
                                    shape = MaterialTheme.shapes.small,
                                    color = showProductDialog!!.category.color.copy(alpha = 0.2f)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.ShoppingCart,
                                            contentDescription = null,
                                            tint = showProductDialog!!.category.color
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
                        
                        ListItem(
                            headlineContent = { Text("SKU: ${showProductDialog!!.code}") },
                            colors = ListItemDefaults.colors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
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
                        showProductDialog?.let { com.example.navitest.model.CartRepository.add(it) }
                        showProductDialog = null
                    }) {
                        Text("Agregar al carrito")
                    }
                }
            )
        }
    }
}