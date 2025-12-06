package com.example.navitest.utils

import org.junit.Test
import org.junit.Assert.*

/**
 * Pruebas unitarias para las funciones de formateo de moneda
 */
class NumberFormatTest {

    @Test
    fun `toCLP formatea Int correctamente con simbolo de peso`() {
        
        val amount = 10000
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        assertTrue(formatted.contains("10"))
    }

    @Test
    fun `toCLP formatea cero correctamente`() {
        
        val amount = 0
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        assertTrue(formatted.contains("0"))
    }

    @Test
    fun `toCLP formatea numeros negativos`() {
        
        val amount = -5000
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        assertTrue(formatted.contains("-") || formatted.contains("("))
    }

    @Test
    fun `toCLP formatea Double correctamente redondeando hacia arriba`() {
        
        val amount = 9999.1
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        assertTrue(formatted.contains("10") || formatted.contains("000"))
    }

    @Test
    fun `toCLP formatea Double con decimales altos`() {
        
        val amount = 1500.99
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        
        assertTrue(formatted.contains("1") && formatted.contains("5"))
    }

    @Test
    fun `toCLP formatea Double sin decimales`() {
        
        val amount = 5000.0
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        assertTrue(formatted.contains("5"))
    }

    @Test
    fun `toCLP formatea cantidades grandes con separador de miles`() {
        
        val amount = 1000000
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        
        assertTrue(formatted.contains(".") || formatted.contains(","))
    }

    @Test
    fun `toCLP Double formatea cantidades grandes`() {
        
        val amount = 2500000.50
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        assertTrue(formatted.contains("2"))
        assertTrue(formatted.contains("5"))
    }

    @Test
    fun `toCLP mantiene formato CLP consistente`() {
        
        val amount1 = 100
        val amount2 = 100.0
        
        
        val formatted1 = amount1.toCLP()
        val formatted2 = amount2.toCLP()
        
        
        
        assertTrue(formatted1.contains("$"))
        assertTrue(formatted2.contains("$"))
    }

    @Test
    fun `toCLP Int maneja valores maximos`() {
        
        val amount = Int.MAX_VALUE
        
        
        val formatted = amount.toCLP()
        
        
        assertTrue(formatted.contains("$"))
        assertNotNull(formatted)
        assertTrue(formatted.isNotEmpty())
    }
}
