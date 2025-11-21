package com.example.guaumiau.data.repository

import com.example.guaumiau.data.model.WeatherResponse
import com.example.guaumiau.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Repositorio para el manejo de datos del clima
 * 
 * Responsabilidades:
 * - Consultar la API de Open-Meteo
 * - Implementar cache temporal para evitar requests innecesarios
 * - Manejar errores de red y timeouts
 * - Proveer datos limpios al ViewModel
 * 
 * Patrón Repository: Abstrae la fuente de datos (API) del ViewModel
 */
class WeatherRepository {
    
    /**
     * Coordenadas de Santiago de Chile
     */
    companion object {
        private const val SANTIAGO_LATITUDE = -33.46
        private const val SANTIAGO_LONGITUDE = -70.65
        
        /**
         * Duración del cache en milisegundos (5 minutos)
         * El clima no cambia drásticamente en pocos minutos,
         * por lo que cachear reduce consumo de datos y mejora performance
         */
        private const val CACHE_DURATION_MS = 5 * 60 * 1000L // 5 minutos
    }
    
    /**
     * Cache de la última respuesta exitosa
     * Volatile garantiza visibilidad entre threads
     */
    @Volatile
    private var cachedWeather: WeatherResponse? = null
    
    /**
     * Timestamp de cuando se obtuvo el último dato cacheado
     */
    @Volatile
    private var cacheTimestamp: Long = 0L
    
    /**
     * Verifica si el cache es válido (no ha expirado)
     * 
     * @return true si hay datos cacheados y no han pasado más de CACHE_DURATION_MS
     */
    private fun isCacheValid(): Boolean {
        val now = System.currentTimeMillis()
        return cachedWeather != null && (now - cacheTimestamp) < CACHE_DURATION_MS
    }
    
    /**
     * Invalida el cache forzando una nueva consulta en el próximo request
     * Útil para implementar botón de "Refrescar"
     */
    fun invalidateCache() {
        cachedWeather = null
        cacheTimestamp = 0L
    }
    
    /**
     * Obtiene el clima actual de Santiago de Chile
     * 
     * Flujo:
     * 1. Verifica si hay cache válido, si sí lo retorna
     * 2. Si no hay cache, consulta la API
     * 3. Maneja diferentes tipos de errores de red
     * 4. Cachea respuesta exitosa
     * 
     * @param forceRefresh Si es true, ignora el cache y hace request fresco
     * @return Result con WeatherResponse si es exitoso, o Exception si falla
     * 
     * Tipos de errores manejados:
     * - UnknownHostException: Sin conexión a internet
     * - SocketTimeoutException: Timeout de conexión
     * - IOException: Error general de red
     * - HTTP errors: Código de estado no 2xx
     * - Parsing errors: JSON inválido o inesperado
     */
    suspend fun getCurrentWeather(forceRefresh: Boolean = false): Result<WeatherResponse> = 
        withContext(Dispatchers.IO) {
            try {
                // Si hay cache válido y no se fuerza refresh, retornar cache
                if (!forceRefresh && isCacheValid()) {
                    return@withContext Result.success(cachedWeather!!)
                }
                
                // Hacer request a la API
                val response = RetrofitClient.weatherApiService.getCurrentWeather(
                    latitude = SANTIAGO_LATITUDE,
                    longitude = SANTIAGO_LONGITUDE
                )
                
                // Verificar si la respuesta es exitosa (código 2xx)
                if (response.isSuccessful) {
                    val weatherData = response.body()
                    
                    if (weatherData != null) {
                        // Cachear datos exitosos
                        cachedWeather = weatherData
                        cacheTimestamp = System.currentTimeMillis()
                        Result.success(weatherData)
                    } else {
                        // Body null (no debería pasar con código 2xx, pero por seguridad)
                        Result.failure(
                            Exception("Respuesta vacía del servidor. Intenta nuevamente.")
                        )
                    }
                } else {
                    // Error HTTP (4xx, 5xx, etc.)
                    val errorMessage = when (response.code()) {
                        400 -> "Solicitud inválida. Verifica los parámetros."
                        404 -> "Servicio no encontrado. Intenta más tarde."
                        429 -> "Demasiadas solicitudes. Espera un momento."
                        500, 502, 503 -> "Servicio temporalmente no disponible. Intenta más tarde."
                        else -> "Error del servidor (${response.code()}). Intenta más tarde."
                    }
                    Result.failure(Exception(errorMessage))
                }
                
            } catch (e: UnknownHostException) {
                // Sin conexión a internet
                Result.failure(
                    IOException("Sin conexión a internet. Verifica tu conexión y vuelve a intentar.")
                )
                
            } catch (e: SocketTimeoutException) {
                // Timeout de conexión o lectura
                Result.failure(
                    IOException("Tiempo de espera agotado. Verifica tu conexión e intenta nuevamente.")
                )
                
            } catch (e: IOException) {
                // Error general de red (DNS, socket, etc.)
                Result.failure(
                    IOException("Error de conexión: ${e.localizedMessage ?: "Problema de red"}. Intenta más tarde.")
                )
                
            } catch (e: Exception) {
                // Cualquier otro error inesperado (parsing JSON, etc.)
                Result.failure(
                    Exception("Error inesperado: ${e.localizedMessage ?: "Error desconocido"}. Intenta nuevamente.")
                )
            }
        }
    
    /**
     * Obtiene el clima actual y retorna un mensaje personalizado
     * según la condición climática
     * 
     * Esta es una función de conveniencia que combina la consulta
     * con la lógica de generación de mensajes.
     * 
     * @param forceRefresh Si es true, ignora el cache
     * @return Result con Pair(WeatherResponse, mensaje personalizado)
     */
    suspend fun getWeatherWithMessage(forceRefresh: Boolean = false): Result<Pair<WeatherResponse, String>> =
        withContext(Dispatchers.IO) {
            val result = getCurrentWeather(forceRefresh)
            
            result.fold(
                onSuccess = { weatherResponse ->
                    val condition = weatherResponse.currentWeather.getCondition()
                    Result.success(Pair(weatherResponse, condition.message))
                },
                onFailure = { exception ->
                    Result.failure(exception)
                }
            )
        }
}
