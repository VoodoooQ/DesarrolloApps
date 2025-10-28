package com.example.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad de Room para almacenar mascotas en la base de datos
 */
@Entity(tableName = "pets")
data class PetEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val type: String, // PERRO, GATO, AVE, OTRO
    val userEmail: String // Email del dueño para filtrar
)
