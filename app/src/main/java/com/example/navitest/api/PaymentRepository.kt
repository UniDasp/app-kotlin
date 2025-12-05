package com.example.navitest.api

import com.example.navitest.model.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PaymentRepository {
    private val api = RetrofitClient.apiService

    suspend fun initiatePayment(
        token: String,
        userId: Int,
        userEmail: String,
        totalAmount: Double,
        paymentMethod: String,
        shippingAddress: String,
        shippingCity: String,
        shippingPhone: String,
        items: List<CartItem>
    ): Result<PaymentResponse> = withContext(Dispatchers.IO) {
        try {
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            
            val products = items.map { item ->
                PaymentProductDto(
                    id = item.productId,
                    code = item.sku ?: "",
                    name = item.name,
                    price = item.price,
                    quantity = item.quantity
                )
            }
            
            val request = InitiatePaymentRequest(
                userId = userId,
                userEmail = userEmail,
                totalAmount = totalAmount,
                paymentMethod = paymentMethod,
                shippingAddress = shippingAddress,
                shippingCity = shippingCity,
                shippingPhone = shippingPhone,
                products = products
            )
            
            val response = api.initiatePayment(authToken, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to initiate payment: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun confirmPayment(
        token: String?,
        paymentId: String,
        cardNumber: String,
        cardholderName: String,
        expirationMonth: String,
        expirationYear: String,
        cvv: String
    ): Result<PaymentResponse> = withContext(Dispatchers.IO) {
        try {
            val request = ConfirmPaymentRequest(
                cardNumber = cardNumber.replace("\\s+".toRegex(), ""),
                cardholderName = cardholderName,
                expirationMonth = expirationMonth,
                expirationYear = expirationYear,
                cvv = cvv,
                cardType = ""
            )
            
            val authToken = if (token != null) {
                if (token.startsWith("Bearer ")) token else "Bearer $token"
            } else null
            
            val response = api.confirmPayment(paymentId, authToken, request)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to confirm payment: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getMyPayments(token: String): Result<List<PaymentResponse>> = withContext(Dispatchers.IO) {
        try {
            val authToken = if (token.startsWith("Bearer ")) token else "Bearer $token"
            val response = api.getMyPayments(authToken)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get payments: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
