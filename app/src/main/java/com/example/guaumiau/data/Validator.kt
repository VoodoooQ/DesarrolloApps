package com.example.guaumiau.data

import java.util.regex.Pattern

/**
 * Objeto Validator con todas las validaciones en tiempo real
 */
object Validator {

    /**
     * Valida nombre completo
     * - No vacío
     * - Solo caracteres alfabéticos y espacios
     * - Máximo 50 caracteres
     */
    fun validateFullName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre completo es obligatorio")
            !name.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) -> 
                ValidationResult(false, "El nombre solo puede contener letras y espacios")
            name.length > 50 -> 
                ValidationResult(false, "El nombre no puede exceder los 50 caracteres")
            else -> ValidationResult(true)
        }
    }

    /**
     * Valida correo electrónico
     * - Formato de correo estándar
     * - Solo dominio @duoc.cl
     */
    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "El correo electrónico es obligatorio")
            !isValidEmailFormat(email) -> 
                ValidationResult(false, "Formato de correo inválido")
            !email.lowercase().endsWith("@duoc.cl") -> 
                ValidationResult(false, "Solo se permiten correos con dominio @duoc.cl")
            else -> ValidationResult(true)
        }
    }

    /**
     * Valida formato de correo electrónico estándar
     */
    private fun isValidEmailFormat(email: String): Boolean {
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
            "@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
        )
        return emailPattern.matcher(email).matches()
    }

    /**
     * Valida contraseña
     * - Mínimo 8 caracteres
     * - Al menos una letra mayúscula
     * - Al menos una letra minúscula
     * - Al menos un número
     * - Al menos un carácter especial (@#$%&*!?_-)
     */
    fun validatePassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "La contraseña es obligatoria")
            password.length < 8 -> 
                ValidationResult(false, "La contraseña debe tener al menos 8 caracteres")
            !password.any { it.isUpperCase() } -> 
                ValidationResult(false, "La contraseña debe contener al menos una mayúscula")
            !password.any { it.isLowerCase() } -> 
                ValidationResult(false, "La contraseña debe contener al menos una minúscula")
            !password.any { it.isDigit() } -> 
                ValidationResult(false, "La contraseña debe contener al menos un número")
            !password.any { it in "@#$%&*!?_-" } -> 
                ValidationResult(false, "La contraseña debe contener al menos un carácter especial (@#$%&*!?_-)")
            else -> ValidationResult(true)
        }
    }

    /**
     * Valida confirmación de contraseña
     */
    fun validatePasswordConfirmation(password: String, confirmation: String): ValidationResult {
        return when {
            confirmation.isBlank() -> 
                ValidationResult(false, "Debe confirmar la contraseña")
            password != confirmation -> 
                ValidationResult(false, "Las contraseñas no coinciden")
            else -> ValidationResult(true)
        }
    }

    /**
     * Valida teléfono (opcional)
     * Si se ingresa, debe ser un número válido
     */
    fun validatePhone(phone: String): ValidationResult {
        if (phone.isBlank()) {
            return ValidationResult(true) // Es opcional
        }
        
        val cleanPhone = phone.replace(Regex("[\\s-]"), "")
        
        return when {
            !cleanPhone.matches(Regex("^[0-9+]+$")) -> 
                ValidationResult(false, "El teléfono solo puede contener números")
            cleanPhone.length < 8 -> 
                ValidationResult(false, "El teléfono debe tener al menos 8 dígitos")
            cleanPhone.length > 15 -> 
                ValidationResult(false, "El teléfono no puede exceder los 15 dígitos")
            else -> ValidationResult(true)
        }
    }

    /**
     * Valida nombre de mascota
     * - Obligatorio
     * - Máximo 50 caracteres
     */
    fun validatePetName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult(false, "El nombre de la mascota es obligatorio")
            name.length > 50 -> 
                ValidationResult(false, "El nombre no puede exceder los 50 caracteres")
            else -> ValidationResult(true)
        }
    }

    /**
     * Valida que se haya seleccionado un tipo de mascota
     */
    fun validatePetType(type: PetType?): ValidationResult {
        return if (type == null) {
            ValidationResult(false, "Debe seleccionar un tipo de mascota")
        } else {
            ValidationResult(true)
        }
    }

    /**
     * Valida credenciales de login (básico)
     */
    fun validateLoginEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult(false, "El correo electrónico es obligatorio")
            else -> ValidationResult(true)
        }
    }

    fun validateLoginPassword(password: String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult(false, "La contraseña es obligatoria")
            else -> ValidationResult(true)
        }
    }
}
