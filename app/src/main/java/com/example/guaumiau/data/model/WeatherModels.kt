package com.example.guaumiau.data.model

import com.google.gson.annotations.SerializedName

/**
 * Enumeración de condiciones climáticas posibles
 * Basado en códigos WMO de Open-Meteo API
 */
enum class WeatherCondition(
    val emoji: String,
    val description: String,
    val message: String
) {
    /**
     * Clima soleado/despejado
     * Códigos WMO: 0
     */
    SUNNY(
        emoji = "☀️",
        description = "Soleado",
        message = "Es un gran día para salir con tu mascota"
    ),
    
    /**
     * Clima parcialmente nublado
     * Códigos WMO: 1, 2
     */
    PARTLY_CLOUDY(
        emoji = "⛅",
        description = "Parcialmente Nublado",
        message = "Podría ser divertido salir, pero lleva un abrigo"
    ),
    
    /**
     * Clima nublado
     * Códigos WMO: 3, 45, 48
     */
    CLOUDY(
        emoji = "☁️",
        description = "Nublado",
        message = "Tal vez no perfecto, pero pueden disfrutar juntos en casa"
    ),
    
    /**
     * Clima lluvioso
     * Códigos WMO: 51, 53, 55, 61, 63, 65, 66, 67, 80, 81, 82
     */
    RAINY(
        emoji = "🌧️",
        description = "Lluvioso",
        message = "Mejor descansa y juega con tu mascota en interiores"
    ),
    
    /**
     * Clima con nieve
     * Códigos WMO: 71, 73, 75, 77, 85, 86
     */
    SNOWY(
        emoji = "❄️",
        description = "Nevando",
        message = "Hace frío afuera, mejor abrígate y disfruta en casa con tu mascota"
    ),
    
    /**
     * Clima con tormenta
     * Códigos WMO: 95, 96, 99
     */
    STORMY(
        emoji = "⛈️",
        description = "Tormenta",
        message = "Quédate en casa, no es seguro salir con tu mascota"
    ),
    
    /**
     * Condición desconocida o no reconocida
     */
    UNKNOWN(
        emoji = "🌡️",
        description = "Desconocido",
        message = "Consulta el clima antes de salir con tu mascota"
    );
    
    companion object {
        /**
         * Convierte un código WMO a una condición climática
         * 
         * Códigos WMO Weather interpretation:
         * 0: Clear sky
         * 1, 2, 3: Mainly clear, partly cloudy, and overcast
         * 45, 48: Fog and depositing rime fog
         * 51, 53, 55: Drizzle: Light, moderate, and dense intensity
         * 56, 57: Freezing Drizzle: Light and dense intensity
         * 61, 63, 65: Rain: Slight, moderate and heavy intensity
         * 66, 67: Freezing Rain: Light and heavy intensity
         * 71, 73, 75: Snow fall: Slight, moderate, and heavy intensity
         * 77: Snow grains
         * 80, 81, 82: Rain showers: Slight, moderate, and violent
         * 85, 86: Snow showers slight and heavy
         * 95: Thunderstorm: Slight or moderate
         * 96, 99: Thunderstorm with slight and heavy hail
         * 
         * @param code Código WMO de condición climática
         * @return Condición climática correspondiente
         */
        fun fromWmoCode(code: Int): WeatherCondition {
            return when (code) {
                0 -> SUNNY
                1, 2 -> PARTLY_CLOUDY
                3, 45, 48 -> CLOUDY
                51, 53, 55, 56, 57, 61, 63, 65, 66, 67, 80, 81, 82 -> RAINY
                71, 73, 75, 77, 85, 86 -> SNOWY
                95, 96, 99 -> STORMY
                else -> UNKNOWN
            }
        }
    }
}

/**
 * Modelo de datos para la respuesta actual del clima
 * Estructura según Open-Meteo API response
 */
data class CurrentWeather(
    /**
     * Temperatura en grados Celsius
     */
    @SerializedName("temperature")
    val temperature: Double,
    
    /**
     * Código WMO de condición climática
     * Ver WeatherCondition.fromWmoCode() para interpretación
     */
    @SerializedName("weathercode")
    val weatherCode: Int,
    
    /**
     * Velocidad del viento en km/h
     */
    @SerializedName("windspeed")
    val windSpeed: Double,
    
    /**
     * Dirección del viento en grados
     */
    @SerializedName("winddirection")
    val windDirection: Int,
    
    /**
     * Timestamp ISO8601 del momento de la medición
     */
    @SerializedName("time")
    val time: String
) {
    /**
     * Obtiene la condición climática basada en el código WMO
     */
    fun getCondition(): WeatherCondition = WeatherCondition.fromWmoCode(weatherCode)
}

/**
 * Modelo de respuesta completo de la API Open-Meteo
 */
data class WeatherResponse(
    /**
     * Latitud de la ubicación consultada
     */
    @SerializedName("latitude")
    val latitude: Double,
    
    /**
     * Longitud de la ubicación consultada
     */
    @SerializedName("longitude")
    val longitude: Double,
    
    /**
     * Zona horaria de la ubicación
     */
    @SerializedName("timezone")
    val timezone: String,
    
    /**
     * Datos del clima actual
     */
    @SerializedName("current_weather")
    val currentWeather: CurrentWeather
)

/**
 * Estado de UI para la vista de clima
 * Maneja los diferentes estados de la consulta
 */
sealed class WeatherUiState {
    /**
     * Estado inicial, sin datos
     */
    object Initial : WeatherUiState()
    
    /**
     * Cargando datos desde la API
     */
    object Loading : WeatherUiState()
    
    /**
     * Datos cargados exitosamente
     * @param weatherResponse Respuesta completa de la API
     * @param condition Condición climática interpretada
     */
    data class Success(
        val weatherResponse: WeatherResponse,
        val condition: WeatherCondition
    ) : WeatherUiState()
    
    /**
     * Error al cargar datos
     * @param message Mensaje de error descriptivo
     * @param canRetry Indica si se puede reintentar la operación
     */
    data class Error(
        val message: String,
        val canRetry: Boolean = true
    ) : WeatherUiState()
}
