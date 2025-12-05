package com.example.navitest.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navitest.model.Product
import com.example.navitest.utils.toCLP

@Composable
fun ProductPrice(
    product: Product,
    modifier: Modifier = Modifier,
    showDealName: Boolean = true
) {
    Column(modifier = modifier) {
        if (product.hasActiveDiscount) {
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                
                product.originalPrice?.let {
                    Text(
                        text = it.toCLP(),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                }

                
                product.discountPercentage?.let {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE74C3C)
                    ) {
                        Text(
                            text = "${it.toInt()}% OFF",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            
            product.discountedPrice?.let {
                Text(
                    text = it.toCLP(),
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE74C3C)
                    )
                )
            }

            
            product.savings?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Ahorras: ${it.toCLP()}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF27AE60)
                    )
                )
            }

            
            if (showDealName && product.activeDealName != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "🎉 ${product.activeDealName}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        } else {
            
            Text(
                text = product.price.toCLP(),
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun CompactProductPrice(
    product: Product,
    modifier: Modifier = Modifier
) {
    if (product.hasActiveDiscount) {
        Column(modifier = modifier) {
            
            product.originalPrice?.let {
                Text(
                    text = it.toCLP(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textDecoration = TextDecoration.LineThrough
                    )
                )
            }

            
            product.discountedPrice?.let {
                Text(
                    text = it.toCLP(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE74C3C)
                    )
                )
            }
        }
    } else {
        Text(
            text = product.price.toCLP(),
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = modifier
        )
    }
}
