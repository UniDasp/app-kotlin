package com.example.navitest.model

import com.google.gson.annotations.SerializedName

data class User(
    val id: Int,
    val username: String? = null,
    
    @SerializedName("firstName")
    val nombre: String? = null,
    
    @SerializedName("lastName")
    val apellido: String? = null,
    
    val email: String,
    
    @SerializedName("phone")
    val telefono: String? = null,
    
    @SerializedName("address")
    val direccion: String? = null,
    
    val region: String? = null,
    
    @SerializedName("city")
    val ciudad: String? = null,
    
    @SerializedName("roles")
    val roles: List<String>? = null,
    
    @SerializedName("enabled")
    val activo: Boolean = true,
    
    val createdAt: String? = null,
    val updatedAt: String? = null
) {
    val fullName: String
        get() = "${nombre ?: ""} ${apellido ?: ""}".trim()
    
    val rol: String
        get() = roles?.firstOrNull()?.removePrefix("ROLE_") ?: "USER"
}
