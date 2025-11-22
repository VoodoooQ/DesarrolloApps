package com.example.guaumiau.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object para Mascota desde la API de Railway
 * 
 * Modelo para intercambio de datos con el backend Java Spring Boot
 * desplegado en https://microservicedm-production.up.railway.app
 */
data class PetDto(
    @SerializedName("id")
    val id: Int? = null,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("userEmail")
    val userEmail: String
)

/**
 * Request para crear una nueva mascota
 */
data class CreatePetRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("userEmail")
    val userEmail: String
)

/**
 * Request para actualizar una mascota existente
 */
data class UpdatePetRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String
)
