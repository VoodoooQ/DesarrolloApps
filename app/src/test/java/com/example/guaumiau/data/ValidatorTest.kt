package com.example.guaumiau.data

import com.example.guaumiau.model.PetType
import org.junit.Assert.*
import org.junit.Test

/**
 * Pruebas unitarias exhaustivas para Validator
 * 
 * Cobertura:
 * - Validación de nombres completos (casos típicos y extremos)
 * - Validación de correos electrónicos (formato, dominio @duoc.cl)
 * - Validación de contraseñas (complejidad, caracteres especiales)
 * - Validación de confirmación de contraseña
 * - Validación de teléfonos (formato, longitud)
 * - Validación de nombres de mascotas
 * - Validación de tipos de mascotas
 * 
 * Estrategia:
 * - Casos válidos (happy path)
 * - Casos inválidos (edge cases)
 * - Valores límite (boundary testing)
 * - Valores null/empty/blank
 */
class ValidatorTest {
    
    // ==================== VALIDACIÓN DE NOMBRE COMPLETO ====================
    
    /**
     * Test: Nombre válido con letras y espacios debe pasar
     */
    @Test
    fun validateFullName_ValidName_ReturnsSuccess() {
        val result = Validator.validateFullName("Juan Pérez García")
        assertTrue("Nombre válido debe pasar", result.isValid)
        assertNull("No debe tener mensaje de error", result.errorMessage)
    }
    
    /**
     * Test: Nombre con caracteres especiales latinos debe pasar
     */
    @Test
    fun validateFullName_WithAccents_ReturnsSuccess() {
        val names = listOf(
            "María José González",
            "José Ángel Núñez",
            "Sofía Mónica Álvarez"
        )
        
        names.forEach { name ->
            val result = Validator.validateFullName(name)
            assertTrue("Nombre con tildes '$name' debe pasar", result.isValid)
        }
    }
    
    /**
     * Test: Nombre vacío debe fallar
     */
    @Test
    fun validateFullName_EmptyName_ReturnsFail() {
        val result = Validator.validateFullName("")
        assertFalse("Nombre vacío debe fallar", result.isValid)
        assertEquals("El nombre completo es obligatorio", result.errorMessage)
    }
    
    /**
     * Test: Nombre solo con espacios debe fallar
     */
    @Test
    fun validateFullName_BlankName_ReturnsFail() {
        val result = Validator.validateFullName("   ")
        assertFalse("Nombre con solo espacios debe fallar", result.isValid)
        assertEquals("El nombre completo es obligatorio", result.errorMessage)
    }
    
    /**
     * Test: Nombre con números debe fallar
     */
    @Test
    fun validateFullName_WithNumbers_ReturnsFail() {
        val invalidNames = listOf(
            "Juan123",
            "María 5to",
            "Pedro2023"
        )
        
        invalidNames.forEach { name ->
            val result = Validator.validateFullName(name)
            assertFalse("Nombre con números '$name' debe fallar", result.isValid)
            assertEquals("El nombre solo puede contener letras y espacios", result.errorMessage)
        }
    }
    
    /**
     * Test: Nombre con caracteres especiales no latinos debe fallar
     */
    @Test
    fun validateFullName_WithSpecialCharacters_ReturnsFail() {
        val invalidNames = listOf(
            "Juan@Pérez",
            "María#González",
            "Pedro-López"
        )
        
        invalidNames.forEach { name ->
            val result = Validator.validateFullName(name)
            assertFalse("Nombre con caracteres especiales '$name' debe fallar", result.isValid)
        }
    }
    
    /**
     * Test: Nombre excesivamente largo debe fallar
     */
    @Test
    fun validateFullName_TooLong_ReturnsFail() {
        val longName = "A".repeat(51)
        val result = Validator.validateFullName(longName)
        assertFalse("Nombre de 51 caracteres debe fallar", result.isValid)
        assertEquals("El nombre no puede exceder los 50 caracteres", result.errorMessage)
    }
    
    /**
     * Test: Nombre de exactamente 50 caracteres debe pasar
     */
    @Test
    fun validateFullName_ExactlyFiftyCharacters_ReturnsSuccess() {
        val name = "A".repeat(50)
        val result = Validator.validateFullName(name)
        assertTrue("Nombre de exactamente 50 caracteres debe pasar", result.isValid)
    }
    
    // ==================== VALIDACIÓN DE EMAIL ====================
    
    /**
     * Test: Email válido @duoc.cl debe pasar
     */
    @Test
    fun validateEmail_ValidDuocEmail_ReturnsSuccess() {
        val validEmails = listOf(
            "juan.perez@duoc.cl",
            "maria123@duoc.cl",
            "estudiante@duoc.cl"
        )
        
        validEmails.forEach { email ->
            val result = Validator.validateEmail(email)
            assertTrue("Email válido '$email' debe pasar", result.isValid)
        }
    }
    
    /**
     * Test: Email vacío debe fallar
     */
    @Test
    fun validateEmail_EmptyEmail_ReturnsFail() {
        val result = Validator.validateEmail("")
        assertFalse("Email vacío debe fallar", result.isValid)
        assertEquals("El correo electrónico es obligatorio", result.errorMessage)
    }
    
    /**
     * Test: Email sin @ debe fallar
     */
    @Test
    fun validateEmail_WithoutAtSymbol_ReturnsFail() {
        val result = Validator.validateEmail("juanduoc.cl")
        assertFalse("Email sin @ debe fallar", result.isValid)
        assertEquals("Formato de correo inválido", result.errorMessage)
    }
    
    /**
     * Test: Email con dominio incorrecto debe fallar
     */
    @Test
    fun validateEmail_WrongDomain_ReturnsFail() {
        val invalidEmails = listOf(
            "juan@gmail.com",
            "maria@hotmail.com",
            "pedro@yahoo.cl"
        )
        
        invalidEmails.forEach { email ->
            val result = Validator.validateEmail(email)
            assertFalse("Email con dominio incorrecto '$email' debe fallar", result.isValid)
            assertEquals("Solo se permiten correos con dominio @duoc.cl", result.errorMessage)
        }
    }
    
    /**
     * Test: Email con formato inválido debe fallar
     */
    @Test
    fun validateEmail_InvalidFormat_ReturnsFail() {
        val invalidEmails = listOf(
            "juan@@duoc.cl",
            "@duoc.cl",
            "juan@",
            "juan perez@duoc.cl"
        )
        
        invalidEmails.forEach { email ->
            val result = Validator.validateEmail(email)
            assertFalse("Email con formato inválido '$email' debe fallar", result.isValid)
        }
    }
    
    /**
     * Test: Email con mayúsculas debe pasar (case insensitive)
     */
    @Test
    fun validateEmail_WithUpperCase_ReturnsSuccess() {
        val result = Validator.validateEmail("Juan.Perez@DUOC.CL")
        assertTrue("Email con mayúsculas debe pasar", result.isValid)
    }
    
    // ==================== VALIDACIÓN DE CONTRASEÑA ====================
    
    /**
     * Test: Contraseña válida con todos los requisitos debe pasar
     */
    @Test
    fun validatePassword_ValidPassword_ReturnsSuccess() {
        val validPasswords = listOf(
            "Abc123@#",
            "Password1!",
            "Secure2023#"
        )
        
        validPasswords.forEach { password ->
            val result = Validator.validatePassword(password)
            assertTrue("Contraseña válida '$password' debe pasar", result.isValid)
        }
    }
    
    /**
     * Test: Contraseña vacía debe fallar
     */
    @Test
    fun validatePassword_EmptyPassword_ReturnsFail() {
        val result = Validator.validatePassword("")
        assertFalse("Contraseña vacía debe fallar", result.isValid)
        assertEquals("La contraseña es obligatoria", result.errorMessage)
    }
    
    /**
     * Test: Contraseña corta (menos de 8 caracteres) debe fallar
     */
    @Test
    fun validatePassword_TooShort_ReturnsFail() {
        val result = Validator.validatePassword("Abc12@")
        assertFalse("Contraseña de 6 caracteres debe fallar", result.isValid)
        assertEquals("La contraseña debe tener al menos 8 caracteres", result.errorMessage)
    }
    
    /**
     * Test: Contraseña sin mayúsculas debe fallar
     */
    @Test
    fun validatePassword_NoUpperCase_ReturnsFail() {
        val result = Validator.validatePassword("abc123@#")
        assertFalse("Contraseña sin mayúsculas debe fallar", result.isValid)
        assertEquals("La contraseña debe contener al menos una mayúscula", result.errorMessage)
    }
    
    /**
     * Test: Contraseña sin minúsculas debe fallar
     */
    @Test
    fun validatePassword_NoLowerCase_ReturnsFail() {
        val result = Validator.validatePassword("ABC123@#")
        assertFalse("Contraseña sin minúsculas debe fallar", result.isValid)
        assertEquals("La contraseña debe contener al menos una minúscula", result.errorMessage)
    }
    
    /**
     * Test: Contraseña sin números debe fallar
     */
    @Test
    fun validatePassword_NoNumbers_ReturnsFail() {
        val result = Validator.validatePassword("Abcdefg@")
        assertFalse("Contraseña sin números debe fallar", result.isValid)
        assertEquals("La contraseña debe contener al menos un número", result.errorMessage)
    }
    
    /**
     * Test: Contraseña sin caracteres especiales debe fallar
     */
    @Test
    fun validatePassword_NoSpecialCharacters_ReturnsFail() {
        val result = Validator.validatePassword("Abc12345")
        assertFalse("Contraseña sin caracteres especiales debe fallar", result.isValid)
        assertTrue(result.errorMessage!!.contains("carácter especial"))
    }
    
    /**
     * Test: Todos los caracteres especiales permitidos deben funcionar
     */
    @Test
    fun validatePassword_AllSpecialCharacters_ReturnsSuccess() {
        val specialChars = listOf('@', '#', '$', '%', '&', '*', '!', '?', '_', '-')
        
        specialChars.forEach { char ->
            val password = "Abc123${char}d"
            val result = Validator.validatePassword(password)
            assertTrue("Contraseña con '$char' debe pasar", result.isValid)
        }
    }
    
    // ==================== VALIDACIÓN DE CONFIRMACIÓN DE CONTRASEÑA ====================
    
    /**
     * Test: Contraseñas coincidentes deben pasar
     */
    @Test
    fun validatePasswordConfirmation_MatchingPasswords_ReturnsSuccess() {
        val result = Validator.validatePasswordConfirmation("Abc123@#", "Abc123@#")
        assertTrue("Contraseñas coincidentes deben pasar", result.isValid)
    }
    
    /**
     * Test: Contraseñas no coincidentes deben fallar
     */
    @Test
    fun validatePasswordConfirmation_NotMatching_ReturnsFail() {
        val result = Validator.validatePasswordConfirmation("Abc123@#", "Abc123@!")
        assertFalse("Contraseñas diferentes deben fallar", result.isValid)
        assertEquals("Las contraseñas no coinciden", result.errorMessage)
    }
    
    /**
     * Test: Confirmación vacía debe fallar
     */
    @Test
    fun validatePasswordConfirmation_EmptyConfirmation_ReturnsFail() {
        val result = Validator.validatePasswordConfirmation("Abc123@#", "")
        assertFalse("Confirmación vacía debe fallar", result.isValid)
        assertEquals("Debe confirmar la contraseña", result.errorMessage)
    }
    
    // ==================== VALIDACIÓN DE TELÉFONO ====================
    
    /**
     * Test: Teléfono válido debe pasar
     */
    @Test
    fun validatePhone_ValidPhone_ReturnsSuccess() {
        val validPhones = listOf(
            "912345678",
            "+56912345678",
            "223456789"
        )
        
        validPhones.forEach { phone ->
            val result = Validator.validatePhone(phone)
            assertTrue("Teléfono válido '$phone' debe pasar", result.isValid)
        }
    }
    
    /**
     * Test: Teléfono con letras debe fallar
     */
    @Test
    fun validatePhone_WithLetters_ReturnsFail() {
        val result = Validator.validatePhone("912ABC678")
        assertFalse("Teléfono con letras debe fallar", result.isValid)
        assertEquals("El teléfono solo puede contener números", result.errorMessage)
    }
    
    /**
     * Test: Teléfono muy corto debe fallar
     */
    @Test
    fun validatePhone_TooShort_ReturnsFail() {
        val result = Validator.validatePhone("1234567")
        assertFalse("Teléfono de 7 dígitos debe fallar", result.isValid)
        assertEquals("El teléfono debe tener al menos 8 dígitos", result.errorMessage)
    }
    
    /**
     * Test: Teléfono muy largo debe fallar
     */
    @Test
    fun validatePhone_TooLong_ReturnsFail() {
        val result = Validator.validatePhone("1234567890123456")
        assertFalse("Teléfono de 16 dígitos debe fallar", result.isValid)
        assertEquals("El teléfono no puede exceder los 15 dígitos", result.errorMessage)
    }
    
    /**
     * Test: Teléfono de exactamente 8 dígitos debe pasar
     */
    @Test
    fun validatePhone_ExactlyEightDigits_ReturnsSuccess() {
        val result = Validator.validatePhone("12345678")
        assertTrue("Teléfono de 8 dígitos debe pasar", result.isValid)
    }
    
    // ==================== VALIDACIÓN DE NOMBRE DE MASCOTA ====================
    
    /**
     * Test: Nombre de mascota válido debe pasar
     */
    @Test
    fun validatePetName_ValidName_ReturnsSuccess() {
        val validNames = listOf(
            "Firulais",
            "Max",
            "Luna Bella"
        )
        
        validNames.forEach { name ->
            val result = Validator.validatePetName(name)
            assertTrue("Nombre de mascota '$name' debe pasar", result.isValid)
        }
    }
    
    /**
     * Test: Nombre de mascota vacío debe fallar
     */
    @Test
    fun validatePetName_EmptyName_ReturnsFail() {
        val result = Validator.validatePetName("")
        assertFalse("Nombre vacío debe fallar", result.isValid)
        assertEquals("El nombre de la mascota es obligatorio", result.errorMessage)
    }
    
    /**
     * Test: Nombre de mascota solo con espacios debe fallar
     */
    @Test
    fun validatePetName_BlankName_ReturnsFail() {
        val result = Validator.validatePetName("   ")
        assertFalse("Nombre con solo espacios debe fallar", result.isValid)
    }
    
    /**
     * Test: Nombre de mascota muy largo debe fallar
     */
    @Test
    fun validatePetName_TooLong_ReturnsFail() {
        val longName = "A".repeat(51)
        val result = Validator.validatePetName(longName)
        assertFalse("Nombre de 51 caracteres debe fallar", result.isValid)
        assertEquals("El nombre no puede exceder los 50 caracteres", result.errorMessage)
    }
    
    // ==================== VALIDACIÓN DE TIPO DE MASCOTA ====================
    
    /**
     * Test: Tipo de mascota válido debe pasar
     */
    @Test
    fun validatePetType_ValidType_ReturnsSuccess() {
        val validTypes = listOf(
            PetType.DOG,
            PetType.CAT,
            PetType.BIRD,
            PetType.OTHER
        )
        
        validTypes.forEach { type ->
            val result = Validator.validatePetType(type)
            assertTrue("Tipo de mascota $type debe pasar", result.isValid)
        }
    }
    
    /**
     * Test: Tipo de mascota null debe fallar
     */
    @Test
    fun validatePetType_NullType_ReturnsFail() {
        val result = Validator.validatePetType(null)
        assertFalse("Tipo null debe fallar", result.isValid)
        assertEquals("Debe seleccionar un tipo de mascota", result.errorMessage)
    }
    
    // ==================== VALIDACIÓN DE LOGIN ====================
    
    /**
     * Test: Email de login vacío debe fallar
     */
    @Test
    fun validateLoginEmail_EmptyEmail_ReturnsFail() {
        val result = Validator.validateLoginEmail("")
        assertFalse("Email vacío debe fallar", result.isValid)
        assertEquals("El correo electrónico es obligatorio", result.errorMessage)
    }
    
    /**
     * Test: Email de login válido debe pasar
     */
    @Test
    fun validateLoginEmail_ValidEmail_ReturnsSuccess() {
        val result = Validator.validateLoginEmail("usuario@duoc.cl")
        assertTrue("Email válido debe pasar", result.isValid)
    }
    
    /**
     * Test: Contraseña de login vacía debe fallar
     */
    @Test
    fun validateLoginPassword_EmptyPassword_ReturnsFail() {
        val result = Validator.validateLoginPassword("")
        assertFalse("Contraseña vacía debe fallar", result.isValid)
        assertEquals("La contraseña es obligatoria", result.errorMessage)
    }
    
    /**
     * Test: Contraseña de login válida debe pasar
     */
    @Test
    fun validateLoginPassword_ValidPassword_ReturnsSuccess() {
        val result = Validator.validateLoginPassword("Abc123@#")
        assertTrue("Contraseña válida debe pasar", result.isValid)
    }
}
