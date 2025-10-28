package com.example.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad para las publicaciones del foro
 */
@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val userEmail: String,          // Email del usuario que publicó
    val userName: String,            // Nombre del usuario
    val description: String,         // Descripción de la publicación
    val imageUri: String?,           // URI de la imagen (puede ser null si es solo texto)
    val timestamp: Long,             // Timestamp de la publicación
    val likes: Int = 0               // Número de likes (para futura implementación)
)
