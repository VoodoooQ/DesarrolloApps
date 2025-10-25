package com.example.guaumiau.data

/**
 * Modelo de datos para Usuario
 */
data class User(
    val id: Int = 0,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String? = null,
    val pets: List<PetRegistration> = emptyList()
)

/**
 * Modelo para registro de mascota durante el registro de usuario
 */
data class PetRegistration(
    val id: Int = 0,
    val name: String = "",
    val type: PetType = PetType.DOG
)

/**
 * Tipos de mascota disponibles
 */
enum class PetType(val displayName: String) {
    DOG("PERRO"),
    CAT("GATO"),
    BIRD("AVE"),
    OTHER("Otro")
}
