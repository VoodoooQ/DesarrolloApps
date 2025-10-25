package com.example.guaumiau.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * Entidad Task para Room Database
 * Representa una tarea/item del usuario
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    val title: String,
    
    val description: String,
    
    val date: Long = System.currentTimeMillis(),
    
    val userId: String, // ID del usuario que creó la tarea
    
    val isCompleted: Boolean = false
) {
    /**
     * Convierte el timestamp a Date
     */
    fun getDateAsDate(): Date = Date(date)
    
    /**
     * Formatea la fecha de forma legible
     */
    fun getFormattedDate(): String {
        val sdf = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        return sdf.format(Date(date))
    }
}
