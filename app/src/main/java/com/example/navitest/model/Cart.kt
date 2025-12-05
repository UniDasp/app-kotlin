package com.example.navitest.model

data class CartItem(
    val itemId: String,          
    val id: Int,                 
    val productId: Int,          
    val name: String,
    val image: String,
    val price: Double,
    val quantity: Int,
    val stock: Int,
    val sku: String? = null,     
    
    val originalPrice: Double? = null,
    val discountedPrice: Double? = null,
    val discountPercentage: Double? = null,
    val activeDealId: Int? = null,
    val activeDealName: String? = null
) {
    
    val hasActiveDiscount: Boolean
        get() = discountedPrice != null && discountedPrice!! > 0 && discountPercentage != null && discountPercentage!! > 0

    
    val finalPrice: Double
        get() = if (hasActiveDiscount) discountedPrice!! else price

    
    val savingsPerItem: Double
        get() = if (hasActiveDiscount && originalPrice != null && discountedPrice != null) originalPrice - discountedPrice!! else 0.0

    
    val totalSavings: Double
        get() = savingsPerItem * quantity
}

