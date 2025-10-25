package com.example.guaumiau.data

/**
 * Modelo de mascota
 */
data class Pet(
    val id: Int,
    val name: String,
    val type: String,
    val breed: String,
    val age: Int,
    val imageUrl: String? = null,
    val description: String? = null
)
