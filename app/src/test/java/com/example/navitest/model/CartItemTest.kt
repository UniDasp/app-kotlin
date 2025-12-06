package com.example.navitest.model

import org.junit.Test
import org.junit.Assert.*

class
CartItemTest {

    @Test
    fun `hasActiveDiscount es true cuando hay descuento valido`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 2,
            stock = 10,
            originalPrice = 1200.0,
            discountedPrice = 800.0,
            discountPercentage = 33.33
        )
        
        
        val hasDiscount = cartItem.hasActiveDiscount
        
        
        assertTrue(hasDiscount)
    }

    @Test
    fun `hasActiveDiscount es false cuando discountedPrice es null`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 2,
            stock = 10
        )
        
        
        val hasDiscount = cartItem.hasActiveDiscount
        
        
        assertFalse(hasDiscount)
    }

    @Test
    fun `hasActiveDiscount es false cuando discountedPrice es 0`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 2,
            stock = 10,
            discountedPrice = 0.0,
            discountPercentage = 0.0
        )
        
        
        val hasDiscount = cartItem.hasActiveDiscount
        
        
        assertFalse(hasDiscount)
    }

    @Test
    fun `finalPrice retorna discountedPrice cuando hay descuento`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 2,
            stock = 10,
            discountedPrice = 800.0,
            discountPercentage = 20.0
        )
        
        
        val finalPrice = cartItem.finalPrice
        
        
        assertEquals(800.0, finalPrice, 0.01)
    }

    @Test
    fun `finalPrice retorna price cuando no hay descuento`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 2,
            stock = 10
        )
        
        
        val finalPrice = cartItem.finalPrice
        
        
        assertEquals(1000.0, finalPrice, 0.01)
    }

    @Test
    fun `savingsPerItem calcula el ahorro por unidad correctamente`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 3,
            stock = 10,
            originalPrice = 1500.0,
            discountedPrice = 1000.0,
            discountPercentage = 33.33
        )
        
        
        val savingsPerItem = cartItem.savingsPerItem
        
        
        assertEquals(500.0, savingsPerItem, 0.01)
    }

    @Test
    fun `savingsPerItem es 0 cuando no hay descuento`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 2,
            stock = 10
        )
        
        
        val savingsPerItem = cartItem.savingsPerItem
        
        
        assertEquals(0.0, savingsPerItem, 0.01)
    }

    @Test
    fun `totalSavings calcula el ahorro total multiplicando por cantidad`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 3,
            stock = 10,
            originalPrice = 1500.0,
            discountedPrice = 1000.0,
            discountPercentage = 33.33
        )
        
        
        val totalSavings = cartItem.totalSavings
        
        
        
        assertEquals(1500.0, totalSavings, 0.01)
    }

    @Test
    fun `totalSavings es 0 cuando no hay descuento`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Test Product",
            image = "image.jpg",
            price = 1000.0,
            quantity = 5,
            stock = 10
        )
        
        
        val totalSavings = cartItem.totalSavings
        
        
        assertEquals(0.0, totalSavings, 0.01)
    }

    @Test
    fun `cart item con deal activo tiene campos correctos`() {
        
        val cartItem = CartItem(
            itemId = "deal-item-1",
            id = 1,
            productId = 123,
            name = "Gaming Headset",
            image = "headset.jpg",
            price = 80000.0,
            quantity = 2,
            stock = 15,
            sku = "HDST-001",
            originalPrice = 100000.0,
            discountedPrice = 80000.0,
            discountPercentage = 20.0,
            activeDealId = 10,
            activeDealName = "Cyber Monday"
        )
        
        
        assertEquals("deal-item-1", cartItem.itemId)
        assertEquals(123, cartItem.productId)
        assertEquals("HDST-001", cartItem.sku)
        assertEquals(10, cartItem.activeDealId)
        assertEquals("Cyber Monday", cartItem.activeDealName)
        assertTrue(cartItem.hasActiveDiscount)
        assertEquals(80000.0, cartItem.finalPrice, 0.01)
        assertEquals(20000.0, cartItem.savingsPerItem, 0.01)
        assertEquals(40000.0, cartItem.totalSavings, 0.01)
    }

    @Test
    fun `cart item con cantidad 1 calcula totales correctamente`() {
        
        val cartItem = CartItem(
            itemId = "item1",
            id = 1,
            productId = 1,
            name = "Single Item",
            image = "item.jpg",
            price = 50000.0,
            quantity = 1,
            stock = 5,
            originalPrice = 60000.0,
            discountedPrice = 50000.0,
            discountPercentage = 16.67
        )
        
        
        val savingsPerItem = cartItem.savingsPerItem
        val totalSavings = cartItem.totalSavings
        
        
        assertEquals(10000.0, savingsPerItem, 0.01)
        assertEquals(10000.0, totalSavings, 0.01)
    }

    @Test
    fun `cart item con stock limitado`() {
        
        val cartItem = CartItem(
            itemId = "limited",
            id = 1,
            productId = 1,
            name = "Limited Stock Item",
            image = "limited.jpg",
            price = 25000.0,
            quantity = 2,
            stock = 2
        )
        
        
        assertEquals(2, cartItem.stock)
        assertEquals(2, cartItem.quantity)
        
    }
}
