package com.example.navitest.model

import org.junit.Test
import org.junit.Assert.*

 
class UserTest {

    @Test
    fun `fullName concatena nombre y apellido correctamente`() {
        
        val user = User(
            id = 1,
            username = "jdoe",
            nombre = "John",
            apellido = "Doe",
            email = "john.doe@example.com"
        )
        
        
        val fullName = user.fullName
        
        
        assertEquals("John Doe", fullName)
    }

    @Test
    fun `fullName maneja nombre null`() {
        
        val user = User(
            id = 1,
            username = "jdoe",
            nombre = null,
            apellido = "Doe",
            email = "john.doe@example.com"
        )
        
        
        val fullName = user.fullName
        
        
        assertEquals("Doe", fullName)
    }

    @Test
    fun `fullName maneja apellido null`() {
        
        val user = User(
            id = 1,
            username = "jdoe",
            nombre = "John",
            apellido = null,
            email = "john.doe@example.com"
        )
        
        
        val fullName = user.fullName
        
        
        assertEquals("John", fullName)
    }

    @Test
    fun `fullName maneja ambos null`() {
        
        val user = User(
            id = 1,
            username = "jdoe",
            nombre = null,
            apellido = null,
            email = "john.doe@example.com"
        )
        
        
        val fullName = user.fullName
        
        
        assertEquals("", fullName)
    }

    @Test
    fun `rol retorna primer rol sin prefijo ROLE`() {
        
        val user = User(
            id = 1,
            username = "admin",
            email = "admin@example.com",
            roles = listOf("ROLE_ADMIN", "ROLE_USER")
        )
        
        
        val rol = user.rol
        
        
        assertEquals("ADMIN", rol)
    }

    @Test
    fun `rol retorna USER cuando roles es null`() {
        
        val user = User(
            id = 1,
            username = "user",
            email = "user@example.com",
            roles = null
        )
        
        
        val rol = user.rol
        
        
        assertEquals("USER", rol)
    }

    @Test
    fun `rol retorna USER cuando roles esta vacio`() {
        
        val user = User(
            id = 1,
            username = "user",
            email = "user@example.com",
            roles = emptyList()
        )
        
        
        val rol = user.rol
        
        
        assertEquals("USER", rol)
    }

    @Test
    fun `usuario con todos los campos completos`() {
        
        val user = User(
            id = 1,
            username = "testuser",
            nombre = "Test",
            apellido = "User",
            email = "test@example.com",
            telefono = "123456789",
            direccion = "Test Street 123",
            region = "Metropolitan",
            ciudad = "Santiago",
            roles = listOf("ROLE_USER"),
            activo = true,
            createdAt = "2024-01-01T00:00:00",
            updatedAt = "2024-01-02T00:00:00"
        )
        
        
        assertEquals(1, user.id)
        assertEquals("testuser", user.username)
        assertEquals("Test User", user.fullName)
        assertEquals("test@example.com", user.email)
        assertEquals("123456789", user.telefono)
        assertEquals("Test Street 123", user.direccion)
        assertEquals("Metropolitan", user.region)
        assertEquals("Santiago", user.ciudad)
        assertEquals("USER", user.rol)
        assertTrue(user.activo)
    }

    @Test
    fun `usuario estudiante con email duocuc`() {
        
        val user = User(
            id = 2,
            username = "student",
            nombre = "Student",
            apellido = "DuocUC",
            email = "student@duocuc.cl",
            roles = listOf("ROLE_STUDENT")
        )
        
        
        assertEquals("student@duocuc.cl", user.email)
        assertTrue(user.email.endsWith("@duocuc.cl"))
        assertEquals("STUDENT", user.rol)
    }

    @Test
    fun `usuario inactivo`() {
        
        val user = User(
            id = 3,
            username = "inactive",
            email = "inactive@example.com",
            activo = false
        )
        
        
        assertFalse(user.activo)
    }

    @Test
    fun `usuario con multiples roles usa el primero`() {
        
        val user = User(
            id = 1,
            username = "multiRole",
            email = "multi@example.com",
            roles = listOf("ROLE_ADMIN", "ROLE_MODERATOR", "ROLE_USER")
        )
        
        
        val rol = user.rol
        
        
        assertEquals("ADMIN", rol)
    }

    @Test
    fun `usuario con campos opcionales null`() {
        
        val user = User(
            id = 1,
            username = "minimal",
            email = "minimal@example.com",
            nombre = null,
            apellido = null,
            telefono = null,
            direccion = null,
            region = null,
            ciudad = null
        )
        
        
        assertNull(user.nombre)
        assertNull(user.apellido)
        assertNull(user.telefono)
        assertNull(user.direccion)
        assertNull(user.region)
        assertNull(user.ciudad)
        assertEquals("", user.fullName)
    }

    @Test
    fun `usuario por defecto es activo`() {
        
        val user = User(
            id = 1,
            username = "default",
            email = "default@example.com"
        )
        
        
        assertTrue(user.activo) 
    }
}
