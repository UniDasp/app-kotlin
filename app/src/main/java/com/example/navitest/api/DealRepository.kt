package com.example.navitest.api

import com.example.navitest.model.Deal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DealRepository {
    private val api = RetrofitClient.apiService

    suspend fun getActiveDeals(): Result<List<Deal>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getActiveDeals()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch deals: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getDealById(id: Int): Result<Deal> = withContext(Dispatchers.IO) {
        try {
            val response = api.getDealById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to fetch deal: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
