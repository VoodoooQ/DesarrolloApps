package com.example.guaumiau.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para Usuario desde la API de Railway
 * 
 * Modelo para intercambio de datos con el backend Java Spring Boot
 */
data class UserDto(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("fullName")
    val fullName: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String? = null, // No se devuelve en respuestas GET
    
    @SerializedName("phone")
    val phone: String? = null
)

/**
 * Request para registro de usuario
 */
data class RegisterUserRequest(
    @SerializedName("fullName")
    val fullName: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String,
    
    @SerializedName("phone")
    val phone: String? = null
)

/**
 * Request para login
 */
data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

/**
 * Respuesta de autenticación
 */
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("user")
    val user: UserDto? = null
)
