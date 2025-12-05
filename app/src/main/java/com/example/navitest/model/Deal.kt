package com.example.navitest.model

import com.google.gson.annotations.SerializedName

enum class DealType {
    ALL,
    CATEGORY,
    SPECIFIC_PRODUCT
}

data class Deal(
    val id: Int,
    val name: String,
    val description: String,
    
    @SerializedName("type")
    val type: DealType,
    
    @SerializedName("categoryId")
    val categoryId: Int? = null,
    
    @SerializedName("productId")
    val productId: Int? = null,
    
    @SerializedName("discountPercentage")
    val discountPercentage: Double,
    
    @SerializedName("startDate")
    val startDate: String,
    
    @SerializedName("endDate")
    val endDate: String,
    
    @SerializedName("active")
    val active: Boolean = true,
    
    @SerializedName("imageUrl")
    val imageUrl: String? = null
) {
    fun getNotificationTitle(): String {
        return when (type) {
            DealType.ALL -> "¡Nueva Oferta! 🎉"
            DealType.CATEGORY -> "¡Nueva Oferta! 🎲"
            DealType.SPECIFIC_PRODUCT -> "¡Oferta Especial! ⭐"
        }
    }

    fun getNotificationBody(): String {
        return description
    }

    fun calculateDiscountedPrice(originalPrice: Double): Double {
        return originalPrice * (1 - discountPercentage / 100.0)
    }
    
    /**
     * Calcula el ahorro
     */
    fun calculateSavings(originalPrice: Double): Double {
        return originalPrice - calculateDiscountedPrice(originalPrice)
    }
}
