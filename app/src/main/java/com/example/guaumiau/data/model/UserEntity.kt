package com.example.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para almacenar usuarios en la base de datos
 */
@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val fullName: String,
    val email: String,
    val password: String, // En producción, hashear la contraseña
    val phone: String? = null
)
