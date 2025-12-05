package com.example.navitest.api

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository {
    private val api = RetrofitClient.apiService

    suspend fun registerDevice(token: String, fcmToken: String, platform: String): Result<MessageResponse> =
        withContext(Dispatchers.IO) {
            try {
                val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                
                Log.d("NotificationRepository", "registerDevice called with:")
                Log.d("NotificationRepository", "  authToken: $authToken")
                Log.d("NotificationRepository", "  fcmToken: $fcmToken")
                Log.d("NotificationRepository", "  platform: $platform")
                
                val requestBody = RegisterDeviceRequest(fcmToken, platform)
                Log.d("NotificationRepository", "  requestBody: $requestBody")
                
                val response = api.registerDevice(authToken, requestBody)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to register device: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    suspend fun removeDevice(token: String, platform: String): Result<MessageResponse> = 
        withContext(Dispatchers.IO) {
            try {
                val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
                val response = api.removeDevice(authToken, platform)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to remove device: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}
