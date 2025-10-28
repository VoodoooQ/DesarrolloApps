package com.example.guaumiau.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Clase para manejar la navegación en Jetpack Compose
 */
class NavigationManager {
    
    var currentRoute by mutableStateOf<String>("login")
        private set
    
    /**
     * Navega a una ruta específica
     */
    fun navigateTo(route: String) {
        currentRoute = route
    }
    
    /**
     * Vuelve atrás (por ahora solo a Home)
     */
    fun navigateBack() {
        currentRoute = "home"
    }
}
