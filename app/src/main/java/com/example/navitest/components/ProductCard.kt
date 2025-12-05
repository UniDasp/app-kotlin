package com.example.navitest.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.navitest.model.Product

@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box {
            Column(modifier = Modifier.padding(8.dp)) {
                
                Box {
                    AsyncImage(
                        model = product.image,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(MaterialTheme.shapes.small),
                        contentScale = ContentScale.Crop,
                        placeholder = ColorPainter(Color(0xFFECEFF1)),
                        error = ColorPainter(Color(0xFFCFD8DC))
                    )

                    
                    if (product.hasActiveDiscount && product.discountPercentage != null) {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(4.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE74C3C)
                        ) {
                            Text(
                                text = "-${product.discountPercentage.toInt()}%",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    minLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                
                CompactProductPrice(product = product)

                
                when {
                    !product.isAvailable -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Sin stock",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFE74C3C),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    product.isLowStock -> {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "¡Solo ${product.stock} disponibles!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFF39C12),
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WideProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            
            Box {
                AsyncImage(
                    model = product.image,
                    contentDescription = product.name,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp)
                        .clip(MaterialTheme.shapes.small),
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color(0xFFECEFF1)),
                    error = ColorPainter(Color(0xFFCFD8DC))
                )

                
                if (product.hasActiveDiscount && product.discountPercentage != null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(4.dp),
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE74C3C)
                    ) {
                        Text(
                            text = "-${product.discountPercentage.toInt()}%",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    CompactProductPrice(product = product)
                }

                
                when {
                    !product.isAvailable -> {
                        Text(
                            text = "Sin stock",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFE74C3C),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    product.isLowStock -> {
                        Text(
                            text = "¡Solo ${product.stock} disponibles!",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFF39C12)
                            )
                        )
                    }
                }
            }
        }
    }
}
