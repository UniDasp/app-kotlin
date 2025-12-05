package com.example.navitest.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.navitest.MainActivity
import com.example.navitest.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class LevelUpFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "FCMService"
        private const val CHANNEL_ID = "levelup_notifications"
        private const val CHANNEL_NAME = "Notificaciones LevelUp"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed FCM token: $token")
        
        
        val sharedPreferences = getSharedPreferences("levelup_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("fcm_token", token).apply()
        
        
        val authToken = sharedPreferences.getString("auth_token", null)
        if (authToken != null) {
            sendTokenToServer(token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        
        Log.d(TAG, "From: ${remoteMessage.from}")
        
        
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            
            
            val notificationType = remoteMessage.data["type"]
            if (notificationType == "deal") {
                handleDealNotification(remoteMessage)
                return
            }
        }
        
        
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            
            val title = it.title ?: "LevelUp Gamer"
            val body = it.body ?: ""
            val imageUrl = it.imageUrl?.toString()
            val actionUrl = remoteMessage.data["actionUrl"]
            
            
            val notificationType = when {
                title.contains("pedido", ignoreCase = true) || title.contains("order", ignoreCase = true) -> 
                    com.example.navitest.model.NotificationType.ORDER
                title.contains("oferta", ignoreCase = true) || title.contains("descuento", ignoreCase = true) || 
                title.contains("promo", ignoreCase = true) -> 
                    com.example.navitest.model.NotificationType.PROMO
                title.contains("producto", ignoreCase = true) || title.contains("nuevo", ignoreCase = true) -> 
                    com.example.navitest.model.NotificationType.PRODUCT
                else -> 
                    com.example.navitest.model.NotificationType.SYSTEM
            }
            
            com.example.navitest.model.NotificationRepository.addNotification(
                com.example.navitest.model.AppNotification(
                    title = title,
                    message = body,
                    imageUrl = imageUrl,
                    actionUrl = actionUrl,
                    type = notificationType
                )
            )
            
            sendNotification(title, body, imageUrl, actionUrl)
        }
    }

    private fun sendNotification(title: String, messageBody: String, imageUrl: String?, actionUrl: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            actionUrl?.let { putExtra("action_url", it) }
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 
            0, 
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        
        
        imageUrl?.let {
            
            
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para notificaciones de LevelUp Gamer"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private fun handleDealNotification(remoteMessage: RemoteMessage) {
        val data = remoteMessage.data
        val notification = remoteMessage.notification
        
        val title = notification?.title ?: "¡Nueva Oferta! 🎉"
        val body = notification?.body ?: "¡No te pierdas esta promoción!"
        val dealId = data["dealId"]
        val dealType = data["dealType"] ?: "ALL"
        val actionUrl = data["actionUrl"]
        
        Log.d(TAG, "Deal notification received - dealId: $dealId, type: $dealType")
        
        
        com.example.navitest.model.NotificationRepository.addNotification(
            com.example.navitest.model.AppNotification(
                title = title,
                message = body,
                imageUrl = notification?.imageUrl?.toString(),
                actionUrl = actionUrl,
                type = com.example.navitest.model.NotificationType.PROMO
            )
        )
        
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("notification_type", "deal")
            putExtra("deal_id", dealId)
            putExtra("deal_type", dealType)
            putExtra("action_url", actionUrl)
        }
        
        val pendingIntent = PendingIntent.getActivity(
            this, 
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_PROMO)
        
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para ofertas y promociones"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
        
        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }

    private fun sendTokenToServer(token: String) {
        
        Log.d(TAG, "Sending token to server: $token")
        
    }
}
