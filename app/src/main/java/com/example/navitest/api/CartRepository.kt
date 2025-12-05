package com.example.navitest.api

import com.example.navitest.model.CartItem

class CartRepository(private val apiService: ApiService) {

    suspend fun getCart(token: String): Result<List<CartItem>> {
        return try {
            val response = apiService.getCart("Bearer $token")
            if (response.isSuccessful && response.body() != null) {
                val cartItems = response.body()!!.items.map { it.toCartItem() }
                Result.success(cartItems)
            } else {
                Result.failure(Exception("Failed to fetch cart: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addItem(token: String, productId: Int, quantity: Int): Result<List<CartItem>> {
        return try {
            val response = apiService.addItemToCart(
                token = "Bearer $token",
                body = AddToCartRequest(productId, quantity)
            )
            if (response.isSuccessful && response.body() != null) {
                val cartItems = response.body()!!.items.map { it.toCartItem() }
                Result.success(cartItems)
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Failed to add item"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateQuantity(token: String, itemId: String, quantity: Int): Result<List<CartItem>> {
        return try {
            val response = apiService.updateCartItem(
                token = "Bearer $token",
                itemId = itemId,
                body = UpdateCartItemRequest(quantity)
            )
            if (response.isSuccessful && response.body() != null) {
                val cartItems = response.body()!!.items.map { it.toCartItem() }
                Result.success(cartItems)
            } else {
                Result.failure(Exception("Failed to update quantity: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeItem(token: String, itemId: String): Result<List<CartItem>> {
        return try {
            val response = apiService.removeCartItem("Bearer $token", itemId)
            if (response.isSuccessful && response.body() != null) {
                val cartItems = response.body()!!.items.map { it.toCartItem() }
                Result.success(cartItems)
            } else {
                Result.failure(Exception("Failed to remove item: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clear(token: String): Result<Unit> {
        return try {
            val response = apiService.clearCart("Bearer $token")
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to clear cart: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun CartItemDto.toCartItem(): CartItem {
        return CartItem(
            itemId = this.id,
            id = this.productId,
            productId = this.productId,
            name = this.productName,
            image = this.productImage,
            price = this.price,
            quantity = this.quantity,
            stock = this.availableStock,
            sku = this.productSku,
            originalPrice = this.originalPrice,
            discountedPrice = this.discountedPrice,
            discountPercentage = this.discountPercentage,
            activeDealId = this.activeDealId,
            activeDealName = this.activeDealName
        )
    }
}
