package com.example.guaumiau.data

/**
 * Resultado de validación de campos
 */
data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String? = null
)
