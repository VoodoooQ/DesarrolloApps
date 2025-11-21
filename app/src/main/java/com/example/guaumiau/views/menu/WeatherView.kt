package com.example.guaumiau.views.menu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guaumiau.data.model.WeatherCondition
import com.example.guaumiau.data.model.WeatherUiState
import com.example.guaumiau.viewmodels.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Vista de Clima para Santiago de Chile
 * 
 * Muestra:
 * - Temperatura actual
 * - Condición climática (soleado, nublado, lluvioso, etc.)
 * - Ícono animado según clima
 * - Mensaje personalizado para salir con mascotas
 * - Botón de refresh
 * - Manejo de estados: Loading, Success, Error
 * 
 * Integrado al menú lateral de la app GuauMiau
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherView(
    viewModel: WeatherViewModel = viewModel()
) {
    // Observar el estado de UI reactivamente
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Gradiente de fondo sutil
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                )
            )
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado con animación de fade in
        AnimatedVisibility(
            visible = true,
            enter = fadeIn() + slideInVertically()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🌤️ Clima en Santiago",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "¿Es buen día para salir con tu mascota?",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Contenido según el estado
        when (val state = uiState) {
            is WeatherUiState.Initial -> {
                // Estado inicial (normalmente no se ve porque auto-carga)
                InitialStateContent()
            }
            
            is WeatherUiState.Loading -> {
                // Mostrar indicador de carga
                LoadingContent()
            }
            
            is WeatherUiState.Success -> {
                // Mostrar datos del clima
                SuccessContent(
                    state = state,
                    viewModel = viewModel,
                    onRefresh = { viewModel.refreshWeather() }
                )
            }
            
            is WeatherUiState.Error -> {
                // Mostrar error con opción de reintentar
                ErrorContent(
                    message = state.message,
                    canRetry = state.canRetry,
                    onRetry = { viewModel.retry() }
                )
            }
        }
    }
}

/**
 * Contenido del estado inicial
 */
@Composable
private fun InitialStateContent() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.WbSunny,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Preparando información del clima...",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

/**
 * Contenido del estado de carga
 */
@Composable
private fun LoadingContent() {
    // Animación de pulso infinito
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(56.dp)
                    .scale(scale),
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
            
            Text(
                text = "Consultando el clima...",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Contenido del estado exitoso
 */
@Composable
private fun SuccessContent(
    state: WeatherUiState.Success,
    viewModel: WeatherViewModel,
    onRefresh: () -> Unit
) {
    val weatherResponse = state.weatherResponse
    val condition = state.condition
    val currentWeather = weatherResponse.currentWeather
    
    // Animación de entrada
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }
    
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + scaleIn()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card principal con temperatura e ícono
            WeatherMainCard(
                temperature = currentWeather.temperature,
                condition = condition,
                viewModel = viewModel
            )
            
            // Card con mensaje personalizado
            WeatherMessageCard(condition = condition)
            
            // Card con detalles adicionales
            WeatherDetailsCard(
                windSpeed = currentWeather.windSpeed,
                windDirection = currentWeather.windDirection,
                time = currentWeather.time
            )
            
            // Botón de refresh
            Button(
                onClick = onRefresh,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Actualizar Clima")
            }
        }
    }
}

/**
 * Card principal con temperatura e ícono del clima
 */
@Composable
private fun WeatherMainCard(
    temperature: Double,
    condition: WeatherCondition,
    viewModel: WeatherViewModel
) {
    // Animación de rotación infinita para el emoji
    val infiniteTransition = rememberInfiniteTransition(label = "rotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rotation"
    )
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = getBackgroundColorForCondition(condition)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Ícono del clima con animación
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = condition.emoji,
                    fontSize = 72.sp,
                    modifier = Modifier.graphicsLayer(rotationZ = rotation)
                )
            }
            
            // Descripción de la condición
            Text(
                text = condition.description,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            // Temperatura
            Text(
                text = viewModel.formatTemperature(temperature),
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
            
            // Categoría de temperatura
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White.copy(alpha = 0.3f)
            ) {
                Text(
                    text = getTemperatureLabel(viewModel.getTemperatureCategory(temperature)),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

/**
 * Card con mensaje personalizado según el clima
 */
@Composable
private fun WeatherMessageCard(condition: WeatherCondition) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ícono de mascota
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            // Mensaje
            Text(
                text = condition.message,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

/**
 * Card con detalles adicionales del clima
 */
@Composable
private fun WeatherDetailsCard(
    windSpeed: Double,
    windDirection: Int,
    time: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Detalles",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Divider()
            
            // Velocidad del viento
            DetailRow(
                icon = Icons.Default.Air,
                label = "Viento",
                value = String.format("%.1f km/h", windSpeed)
            )
            
            // Dirección del viento
            DetailRow(
                icon = Icons.Default.Explore,
                label = "Dirección",
                value = "${windDirection}° ${getWindDirectionLabel(windDirection)}"
            )
            
            // Última actualización
            DetailRow(
                icon = Icons.Default.Schedule,
                label = "Actualizado",
                value = formatTime(time)
            )
        }
    }
}

/**
 * Fila de detalle con ícono, etiqueta y valor
 */
@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Contenido del estado de error
 */
@Composable
private fun ErrorContent(
    message: String,
    canRetry: Boolean,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.error
            )
            
            Text(
                text = "Error al consultar el clima",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = message,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onErrorContainer,
                textAlign = TextAlign.Center
            )
            
            if (canRetry) {
                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Reintentar")
                }
            }
        }
    }
}

/**
 * Obtiene el color de fondo según la condición climática
 */
@Composable
private fun getBackgroundColorForCondition(condition: WeatherCondition): Color {
    return when (condition) {
        WeatherCondition.SUNNY -> Color(0xFF4FC3F7) // Azul cielo
        WeatherCondition.PARTLY_CLOUDY -> Color(0xFF81C784) // Verde claro
        WeatherCondition.CLOUDY -> Color(0xFF90A4AE) // Gris azulado
        WeatherCondition.RAINY -> Color(0xFF5C6BC0) // Azul oscuro
        WeatherCondition.SNOWY -> Color(0xFFB0BEC5) // Gris claro
        WeatherCondition.STORMY -> Color(0xFF7E57C2) // Púrpura
        WeatherCondition.UNKNOWN -> MaterialTheme.colorScheme.tertiary
    }
}

/**
 * Obtiene etiqueta en español para categoría de temperatura
 */
private fun getTemperatureLabel(category: String): String {
    return when (category) {
        "cold" -> "Frío ❄️"
        "cool" -> "Fresco 🍃"
        "warm" -> "Templado 🌤️"
        "hot" -> "Caluroso ☀️"
        else -> "Normal"
    }
}

/**
 * Obtiene etiqueta de dirección del viento según grados
 */
private fun getWindDirectionLabel(degrees: Int): String {
    return when (degrees) {
        in 0..22, in 338..360 -> "N"
        in 23..67 -> "NE"
        in 68..112 -> "E"
        in 113..157 -> "SE"
        in 158..202 -> "S"
        in 203..247 -> "SW"
        in 248..292 -> "W"
        in 293..337 -> "NW"
        else -> "N/A"
    }
}

/**
 * Formatea el timestamp ISO8601 a hora legible
 */
private fun formatTime(isoTime: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = inputFormat.parse(isoTime)
        date?.let { outputFormat.format(it) } ?: isoTime
    } catch (e: Exception) {
        isoTime
    }
}
