package com.example.guaumiau.data.model

import org.junit.Assert.*
import org.junit.Test

/**
 * Tests unitarios para WeatherCondition
 * 
 * Verifica:
 * - Conversión correcta de códigos WMO a condiciones climáticas
 * - Mensajes personalizados según condición
 * - Casos extremos y condiciones no reconocidas
 */
class WeatherConditionTest {
    
    /**
     * Test: Código 0 (cielo despejado) debe retornar SUNNY
     */
    @Test
    fun testFromWmoCode_ClearSky_ReturnsSunny() {
        val condition = WeatherCondition.fromWmoCode(0)
        assertEquals(WeatherCondition.SUNNY, condition)
        assertEquals("☀️", condition.emoji)
        assertEquals("Soleado", condition.description)
        assertEquals("Es un gran día para salir con tu mascota", condition.message)
    }
    
    /**
     * Test: Códigos 1 y 2 (parcialmente nublado) deben retornar PARTLY_CLOUDY
     */
    @Test
    fun testFromWmoCode_PartlyCloudy_ReturnsPartlyCloudy() {
        val condition1 = WeatherCondition.fromWmoCode(1)
        val condition2 = WeatherCondition.fromWmoCode(2)
        
        assertEquals(WeatherCondition.PARTLY_CLOUDY, condition1)
        assertEquals(WeatherCondition.PARTLY_CLOUDY, condition2)
        assertEquals("⛅", condition1.emoji)
        assertEquals("Podría ser divertido salir, pero lleva un abrigo", condition1.message)
    }
    
    /**
     * Test: Códigos 3, 45, 48 (nublado/niebla) deben retornar CLOUDY
     */
    @Test
    fun testFromWmoCode_Cloudy_ReturnsCloudy() {
        val condition3 = WeatherCondition.fromWmoCode(3)
        val condition45 = WeatherCondition.fromWmoCode(45)
        val condition48 = WeatherCondition.fromWmoCode(48)
        
        assertEquals(WeatherCondition.CLOUDY, condition3)
        assertEquals(WeatherCondition.CLOUDY, condition45)
        assertEquals(WeatherCondition.CLOUDY, condition48)
        assertEquals("☁️", condition3.emoji)
        assertEquals("Tal vez no perfecto, pero pueden disfrutar juntos en casa", condition3.message)
    }
    
    /**
     * Test: Códigos de lluvia (51-67, 80-82) deben retornar RAINY
     */
    @Test
    fun testFromWmoCode_Rainy_ReturnsRainy() {
        val rainCodes = listOf(51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82)
        
        rainCodes.forEach { code ->
            val condition = WeatherCondition.fromWmoCode(code)
            assertEquals("Código $code debe ser RAINY", WeatherCondition.RAINY, condition)
        }
        
        val rainyCondition = WeatherCondition.fromWmoCode(61)
        assertEquals("🌧️", rainyCondition.emoji)
        assertEquals("Mejor descansa y juega con tu mascota en interiores", rainyCondition.message)
    }
    
    /**
     * Test: Códigos de nieve (71-77, 85-86) deben retornar SNOWY
     */
    @Test
    fun testFromWmoCode_Snowy_ReturnsSnowy() {
        val snowCodes = listOf(71, 73, 75, 77, 85, 86)
        
        snowCodes.forEach { code ->
            val condition = WeatherCondition.fromWmoCode(code)
            assertEquals("Código $code debe ser SNOWY", WeatherCondition.SNOWY, condition)
        }
        
        val snowyCondition = WeatherCondition.fromWmoCode(71)
        assertEquals("❄️", snowyCondition.emoji)
        assertEquals("Hace frío afuera, mejor abrígate y disfruta en casa con tu mascota", snowyCondition.message)
    }
    
    /**
     * Test: Códigos de tormenta (95, 96, 99) deben retornar STORMY
     */
    @Test
    fun testFromWmoCode_Stormy_ReturnsStormy() {
        val stormCodes = listOf(95, 96, 99)
        
        stormCodes.forEach { code ->
            val condition = WeatherCondition.fromWmoCode(code)
            assertEquals("Código $code debe ser STORMY", WeatherCondition.STORMY, condition)
        }
        
        val stormyCondition = WeatherCondition.fromWmoCode(95)
        assertEquals("⛈️", stormyCondition.emoji)
        assertEquals("Quédate en casa, no es seguro salir con tu mascota", stormyCondition.message)
    }
    
    /**
     * Test: Códigos no reconocidos deben retornar UNKNOWN
     */
    @Test
    fun testFromWmoCode_UnknownCode_ReturnsUnknown() {
        val unknownCodes = listOf(-1, 100, 200, 999)
        
        unknownCodes.forEach { code ->
            val condition = WeatherCondition.fromWmoCode(code)
            assertEquals("Código $code debe ser UNKNOWN", WeatherCondition.UNKNOWN, condition)
        }
        
        val unknownCondition = WeatherCondition.fromWmoCode(999)
        assertEquals("🌡️", unknownCondition.emoji)
        assertEquals("Consulta el clima antes de salir con tu mascota", unknownCondition.message)
    }
    
    /**
     * Test: Todos los mensajes deben ser únicos y descriptivos
     */
    @Test
    fun testAllConditions_HaveUniqueMessages() {
        val conditions = WeatherCondition.values()
        val messages = conditions.map { it.message }.toSet()
        
        // Verificar que cada condición tiene un mensaje único
        assertEquals(conditions.size, messages.size)
        
        // Verificar que todos los mensajes no están vacíos
        conditions.forEach { condition ->
            assertTrue("Mensaje de ${condition.name} no debe estar vacío", 
                condition.message.isNotBlank())
        }
    }
    
    /**
     * Test: Todos los emojis deben estar presentes
     */
    @Test
    fun testAllConditions_HaveEmojis() {
        val conditions = WeatherCondition.values()
        
        conditions.forEach { condition ->
            assertTrue("Emoji de ${condition.name} no debe estar vacío", 
                condition.emoji.isNotBlank())
        }
    }
    
    /**
     * Test: CurrentWeather.getCondition() debe retornar la condición correcta
     */
    @Test
    fun testCurrentWeather_GetCondition_ReturnsCorrectCondition() {
        val currentWeather = CurrentWeather(
            temperature = 18.5,
            weatherCode = 0,
            windSpeed = 10.0,
            windDirection = 180,
            time = "2023-11-21T14:00"
        )
        
        assertEquals(WeatherCondition.SUNNY, currentWeather.getCondition())
    }
}
