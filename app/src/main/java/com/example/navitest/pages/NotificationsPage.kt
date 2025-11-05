package com.example.navitest.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class Notification(
    val title: String,
    val description: String,
    val time: String,
    val type: NotificationType,
    val isRead: Boolean
)

enum class NotificationType(val icon: ImageVector, val color: androidx.compose.ui.graphics.Color) {
    ORDER(Icons.Default.ShoppingCart, androidx.compose.ui.graphics.Color(0xFF2196F3)),
    PROMO(Icons.Default.Favorite, androidx.compose.ui.graphics.Color(0xFFFFC107)),
    SYSTEM(Icons.Default.Info, androidx.compose.ui.graphics.Color(0xFF4CAF50))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsPage(modifier: Modifier = Modifier) {
    val notifications = remember {
        listOf(
            Notification(
                "Esto no funciona, pero hagamos que si...",
                "Lo que dice arriba",
                "Hace 1 segundo",
                NotificationType.SYSTEM,
                false
            ),
            Notification(
                "Nuevo pedido #123",
                "Tu pedido ha sido confirmado y está en proceso",
                "Hace 5 min",
                NotificationType.ORDER,
                false
            ),
            Notification(
                "¡50% de descuento!",
                "Aprovecha las ofertas de fin de semana",
                "Hace 2 horas",
                NotificationType.PROMO,
                true
            ),
            Notification(
                "Actualización de la app",
                "Nueva versión disponible con mejoras",
                "Hace 1 día",
                NotificationType.SYSTEM,
                true
            ),
            Notification(
                "Pedido entregado",
                "Tu pedido #120 ha sido entregado",
                "Hace 2 días",
                NotificationType.ORDER,
                true
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notificaciones") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Delete, contentDescription = "Limpiar todo")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            Text(
                "esto no funciona aun :3",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(12.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(notifications) { notification ->
                    ListItem(
                        modifier = Modifier.background(
                            if (!notification.isRead)
                                MaterialTheme.colorScheme.surfaceVariant
                            else MaterialTheme.colorScheme.surface
                        ),
                        headlineContent = { 
                            Text(
                                notification.title,
                                fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        supportingContent = { Text(notification.description) },
                        leadingContent = {
                            Icon(
                                notification.type.icon,
                                contentDescription = null,
                                tint = notification.type.color,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        trailingContent = { 
                            Text(
                                notification.time,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                    Divider()
                }
            }
        }
    }
}