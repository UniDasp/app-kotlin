package com.example.navitest.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.navitest.model.NotificationRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsPage(modifier: Modifier = Modifier) {
    val notifications = NotificationRepository.notifications
    val unreadCount = NotificationRepository.getUnreadCount()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Notificaciones")
                        if (unreadCount > 0) {
                            Text(
                                "$unreadCount sin leer",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                actions = {
                    if (notifications.isNotEmpty()) {
                        IconButton(onClick = { NotificationRepository.markAllAsRead() }) {
                            Icon(Icons.Default.Done, contentDescription = "Marcar todas como leídas")
                        }
                        IconButton(onClick = { NotificationRepository.clearAll() }) {
                            Icon(Icons.Default.Delete, contentDescription = "Limpiar todo")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (notifications.isEmpty()) {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Column(
                    horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "No tienes notificaciones",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(1.dp)
            ) {
                items(notifications, key = { it.id }) { notification ->
                    ListItem(
                        modifier = Modifier
                            .background(
                                if (!notification.isRead)
                                    MaterialTheme.colorScheme.surfaceVariant
                                else MaterialTheme.colorScheme.surface
                            )
                            .clickable {
                                NotificationRepository.markAsRead(notification.id)
                                
                            },
                        headlineContent = { 
                            Text(
                                notification.title,
                                fontWeight = if (!notification.isRead) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        supportingContent = { Text(notification.message) },
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
                                NotificationRepository.getRelativeTime(notification.timestamp),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}
