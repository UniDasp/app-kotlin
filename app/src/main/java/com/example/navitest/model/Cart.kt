package com.example.navitest.model

import androidx.compose.runtime.mutableStateListOf

data class CartItem(
    val product: Product,
    var quantity: Int = 1
)

object CartRepository {
    private val _items = mutableStateListOf<CartItem>()
    val items: List<CartItem> get() = _items

    fun add(product: Product) {
        val existing = _items.find { it.product.id == product.id }
        if (existing != null) {
            existing.quantity += 1
        } else {
            _items.add(CartItem(product))
        }
    }

    fun remove(product: Product) {
        val existing = _items.find { it.product.id == product.id }
        if (existing != null) {
            _items.remove(existing)
        }
    }

    fun updateQuantity(product: Product, qty: Int) {
        val existing = _items.find { it.product.id == product.id }
        if (existing != null) {
            if (qty <= 0) _items.remove(existing) else existing.quantity = qty
        }
    }

    fun clear() {
        _items.clear()
    }
}
