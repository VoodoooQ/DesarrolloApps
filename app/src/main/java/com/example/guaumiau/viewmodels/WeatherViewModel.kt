package com.example.guaumiau.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guaumiau.data.model.WeatherCondition
import com.example.guaumiau.data.model.WeatherUiState
import com.example.guaumiau.data.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de clima
 * 
 * Responsabilidades:
 * - Manejar el estado de UI (Loading, Success, Error)
 * - Coordinar llamadas al repositorio
 * - Exponer datos reactivos vía StateFlow
 * - Manejar lógica de refresh y retry
 * 
 * Arquitectura MVVM:
 * - View: WeatherView (Compose UI)
 * - ViewModel: WeatherViewModel (este archivo)
 * - Model: WeatherRepository + WeatherApiService
 */
class WeatherViewModel(
    private val repository: WeatherRepository = WeatherRepository()
) : ViewModel() {
    
    /**
     * Estado mutable interno del UI
     * Solo el ViewModel puede modificarlo
     */
    private val _uiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    
    /**
     * Estado inmutable expuesto a la UI
     * La vista observa este StateFlow para actualizaciones reactivas
     */
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    /**
     * Flag para evitar múltiples requests simultáneos
     */
    private var isLoading = false
    
    init {
        // Cargar clima automáticamente al crear el ViewModel
        loadWeather()
    }
    
    /**
     * Carga el clima actual de Santiago
     * 
     * Flujo:
     * 1. Verifica que no haya un request en curso
     * 2. Cambia estado a Loading
     * 3. Consulta repositorio (que maneja cache)
     * 4. Actualiza estado según resultado (Success o Error)
     * 
     * @param forceRefresh Si es true, ignora cache y hace request fresco
     */
    fun loadWeather(forceRefresh: Boolean = false) {
        // Evitar múltiples requests simultáneos
        if (isLoading) return
        
        isLoading = true
        _uiState.value = WeatherUiState.Loading
        
        viewModelScope.launch {
            try {
                // Consultar repositorio
                val result = repository.getCurrentWeather(forceRefresh)
                
                // Procesar resultado
                result.fold(
                    onSuccess = { weatherResponse ->
                        // Obtener condición climática del código WMO
                        val condition = weatherResponse.currentWeather.getCondition()
                        
                        // Actualizar UI con datos exitosos
                        _uiState.value = WeatherUiState.Success(
                            weatherResponse = weatherResponse,
                            condition = condition
                        )
                    },
                    onFailure = { exception ->
                        // Actualizar UI con error
                        _uiState.value = WeatherUiState.Error(
                            message = exception.message ?: "Error desconocido",
                            canRetry = true
                        )
                    }
                )
            } catch (e: Exception) {
                // Manejar cualquier error inesperado
                _uiState.value = WeatherUiState.Error(
                    message = "Error inesperado: ${e.localizedMessage ?: "Error desconocido"}",
                    canRetry = true
                )
            } finally {
                isLoading = false
            }
        }
    }
    
    /**
     * Refresca el clima ignorando el cache
     * 
     * Se usa cuando el usuario pulsa el botón de "Refrescar"
     * para obtener datos actualizados de la API
     */
    fun refreshWeather() {
        loadWeather(forceRefresh = true)
    }
    
    /**
     * Reintenta cargar el clima después de un error
     * 
     * Similar a loadWeather() pero más explícito en la intención
     */
    fun retry() {
        loadWeather(forceRefresh = false)
    }
    
    /**
     * Obtiene un mensaje personalizado según la condición climática
     * 
     * Esta función es útil para obtener el mensaje directamente
     * sin tener que extraerlo del estado UI
     * 
     * @param condition Condición climática
     * @return Mensaje personalizado para la condición
     */
    fun getMessageForCondition(condition: WeatherCondition): String {
        return condition.message
    }
    
    /**
     * Formatea la temperatura con el símbolo de grados Celsius
     * 
     * @param temperature Temperatura en grados Celsius
     * @return String formateado (ej: "18.5°C")
     */
    fun formatTemperature(temperature: Double): String {
        return String.format("%.1f°C", temperature)
    }
    
    /**
     * Determina el color sugerido según la temperatura
     * 
     * Útil para dar feedback visual de si hace frío o calor
     * 
     * @param temperature Temperatura en grados Celsius
     * @return String describiendo el rango ("cold", "cool", "warm", "hot")
     */
    fun getTemperatureCategory(temperature: Double): String {
        return when {
            temperature < 10 -> "cold"      // Frío
            temperature < 18 -> "cool"      // Fresco
            temperature < 25 -> "warm"      // Templado
            else -> "hot"                   // Caluroso
        }
    }
    
    /**
     * Limpia el estado de UI al estado inicial
     * 
     * Útil si se necesita resetear la vista
     */
    fun clearState() {
        _uiState.value = WeatherUiState.Initial
    }
}
