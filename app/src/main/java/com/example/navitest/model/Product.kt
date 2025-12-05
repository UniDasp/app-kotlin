package com.example.navitest.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,
    val code: String? = null,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val stock: Int,
    
    @SerializedName("categoryId")
    val categoryId: Int,
    
    @SerializedName("category")
    val category: String? = null,
    
    @SerializedName("categoryName")
    val categoryNameAlt: String? = null,
    
    val featured: Boolean = false,
    
    
    @SerializedName("originalPrice")
    val originalPrice: Double? = null,
    
    @SerializedName("discountedPrice")
    val discountedPrice: Double? = null,
    
    @SerializedName("discountPercentage")
    val discountPercentage: Double? = null,
    
    @SerializedName("activeDealId")
    val activeDealId: Int? = null,
    
    @SerializedName("activeDealName")
    val activeDealName: String? = null,
    
    val active: Boolean = true,
    
    @SerializedName("createdAt")
    val createdAt: String? = null,
    
    @SerializedName("updatedAt")
    val updatedAt: String? = null
) {
    /**
     * Obtiene el nombre de la categoría (prioriza 'category' sobre 'categoryName')
     */
    val categoryName: String
        get() = category ?: categoryNameAlt ?: "Sin categoría"
    
    /**
     * Verifica si el producto tiene un descuento activo
     */
    val hasActiveDiscount: Boolean
        get() = discountedPrice != null && discountPercentage != null
    
    /**
     * Obtiene el precio final a mostrar (con descuento si existe)
     */
    val finalPrice: Double
        get() = discountedPrice ?: price
    
    /**
     * Calcula el ahorro si hay descuento
     */
    val savings: Double?
        get() = if (hasActiveDiscount && originalPrice != null && discountedPrice != null) {
            originalPrice - discountedPrice
        } else null
    
    /**
     * Verifica si el producto está disponible
     */
    val isAvailable: Boolean
        get() = stock > 0
    
    /**
     * Verifica si el stock es bajo (menos de 5 unidades)
     */
    val isLowStock: Boolean
        get() = stock in 1..4
}

