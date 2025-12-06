package com.example.navitest.model

import org.junit.Test
import org.junit.Assert.*

 
class ProductTest {

    @Test
    fun `categoryName retorna category cuando esta disponible`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1,
            category = "Electronics"
        )
        
        
        val categoryName = product.categoryName
        
        
        assertEquals("Electronics", categoryName)
    }

    @Test
    fun `categoryName retorna categoryNameAlt cuando category es null`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1,
            category = null,
            categoryNameAlt = "Alternative Category"
        )
        
        
        val categoryName = product.categoryName
        
        
        assertEquals("Alternative Category", categoryName)
    }

    @Test
    fun `categoryName retorna Sin categoria cuando ambos son null`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1,
            category = null,
            categoryNameAlt = null
        )
        
        
        val categoryName = product.categoryName
        
        
        assertEquals("Sin categoría", categoryName)
    }

    @Test
    fun `hasActiveDiscount es true cuando hay descuento`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1,
            discountedPrice = 800.0,
            discountPercentage = 20.0
        )
        
        
        val hasDiscount = product.hasActiveDiscount
        
        
        assertTrue(hasDiscount)
    }

    @Test
    fun `hasActiveDiscount es false cuando no hay descuento`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1
        )
        
        
        val hasDiscount = product.hasActiveDiscount
        
        
        assertFalse(hasDiscount)
    }

    @Test
    fun `finalPrice retorna discountedPrice cuando hay descuento`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1,
            discountedPrice = 800.0,
            discountPercentage = 20.0
        )
        
        
        val finalPrice = product.finalPrice
        
        
        assertEquals(800.0, finalPrice, 0.01)
    }

    @Test
    fun `finalPrice retorna price cuando no hay descuento`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1
        )
        
        
        val finalPrice = product.finalPrice
        
        
        assertEquals(1000.0, finalPrice, 0.01)
    }

    @Test
    fun `savings calcula el ahorro correctamente`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1,
            originalPrice = 1200.0,
            discountedPrice = 800.0,
            discountPercentage = 33.33
        )
        
        
        val savings = product.savings
        
        
        assertNotNull(savings)
        assertEquals(400.0, savings!!, 0.01)
    }

    @Test
    fun `savings es null cuando no hay descuento`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1
        )
        
        
        val savings = product.savings
        
        
        assertNull(savings)
    }

    @Test
    fun `isAvailable es true cuando hay stock`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 5,
            categoryId = 1
        )
        
        
        val isAvailable = product.isAvailable
        
        
        assertTrue(isAvailable)
    }

    @Test
    fun `isAvailable es false cuando no hay stock`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 0,
            categoryId = 1
        )
        
        
        val isAvailable = product.isAvailable
        
        
        assertFalse(isAvailable)
    }

    @Test
    fun `isLowStock es true cuando stock esta entre 1 y 4`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 3,
            categoryId = 1
        )
        
        
        val isLowStock = product.isLowStock
        
        
        assertTrue(isLowStock)
    }

    @Test
    fun `isLowStock es false cuando stock es 0`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 0,
            categoryId = 1
        )
        
        
        val isLowStock = product.isLowStock
        
        
        assertFalse(isLowStock)
    }

    @Test
    fun `isLowStock es false cuando stock es mayor a 4`() {
        
        val product = Product(
            id = 1,
            name = "Test Product",
            description = "Description",
            price = 1000.0,
            image = "image.jpg",
            stock = 10,
            categoryId = 1
        )
        
        
        val isLowStock = product.isLowStock
        
        
        assertFalse(isLowStock)
    }

    @Test
    fun `producto con deal activo tiene todos los campos correctos`() {
        
        val product = Product(
            id = 1,
            name = "Gaming Mouse",
            description = "High DPI gaming mouse",
            price = 50000.0,
            image = "mouse.jpg",
            stock = 15,
            categoryId = 3,
            category = "Accessories",
            featured = true,
            originalPrice = 60000.0,
            discountedPrice = 50000.0,
            discountPercentage = 16.67,
            activeDealId = 5,
            activeDealName = "Black Friday"
        )
        
        
        assertEquals(5, product.activeDealId)
        assertEquals("Black Friday", product.activeDealName)
        assertTrue(product.hasActiveDiscount)
        assertEquals(50000.0, product.finalPrice, 0.01)
        assertEquals(10000.0, product.savings, 0.01)
    }
}
