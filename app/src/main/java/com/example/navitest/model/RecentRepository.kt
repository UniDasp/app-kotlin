package com.example.navitest.model

import androidx.compose.runtime.mutableStateListOf

object RecentRepository {
    private val _items = mutableStateListOf<Product>()
    val items: List<Product> get() = _items

    fun add(product: Product) {
        // Remove existing occurrence
        _items.removeAll { it.id == product.id }
        // Add to front
        _items.add(0, product)
        // Keep max 10
        if (_items.size > 10) {
            _items.removeLast()
        }
    }

    fun clear() {
        _items.clear()
    }
}
