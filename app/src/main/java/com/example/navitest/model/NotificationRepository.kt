package com.example.navitest.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import java.text.SimpleDateFormat
import java.util.*

data class AppNotification(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val message: String,
    val imageUrl: String? = null,
    val actionUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.SYSTEM
)

enum class NotificationType(val icon: ImageVector, val color: Color) {
    ORDER(Icons.Default.ShoppingCart, Color(0xFF2196F3)),
    PROMO(Icons.Default.Favorite, Color(0xFFFFC107)),
    SYSTEM(Icons.Default.Info, Color(0xFF4CAF50)),
    PRODUCT(Icons.Default.Star, Color(0xFFFF5722))
}

object NotificationRepository {
    private val _notifications = mutableStateListOf<AppNotification>()
    val notifications: List<AppNotification> get() = _notifications

    fun addNotification(notification: AppNotification) {
        _notifications.add(0, notification)
        
        
        if (_notifications.size > 50) {
            _notifications.removeAt(_notifications.size - 1)
        }
    }

    fun markAsRead(id: String) {
        val index = _notifications.indexOfFirst { it.id == id }
        if (index != -1) {
            _notifications[index] = _notifications[index].copy(isRead = true)
        }
    }

    fun markAllAsRead() {
        for (i in _notifications.indices) {
            _notifications[i] = _notifications[i].copy(isRead = true)
        }
    }

    fun clearAll() {
        _notifications.clear()
    }

    fun getUnreadCount(): Int {
        return _notifications.count { !it.isRead }
    }

    fun getRelativeTime(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60_000 -> "Hace ${diff / 1000}s"
            diff < 3600_000 -> "Hace ${diff / 60_000}m"
            diff < 86400_000 -> "Hace ${diff / 3600_000}h"
            diff < 604800_000 -> "Hace ${diff / 86400_000}d"
            else -> {
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
    }
}
