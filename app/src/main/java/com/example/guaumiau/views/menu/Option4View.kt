package com.example.guaumiau.views.menu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vista de Persistencia y Animaciones (Opción 4)
 */
@Composable
fun Option4View() {
    var animateBox by remember { mutableStateOf(false) }
    var animateColor by remember { mutableStateOf(false) }
    var animateSize by remember { mutableStateOf(false) }
    var counter by remember { mutableStateOf(0) }
    
    val boxOffset by animateDpAsState(
        targetValue = if (animateBox) 200.dp else 0.dp,
        animationSpec = tween(durationMillis = 500),
        label = "box offset"
    )
    
    val boxColor by animateColorAsState(
        targetValue = if (animateColor) Color.Green else MaterialTheme.colorScheme.primary,
        animationSpec = tween(durationMillis = 500),
        label = "box color"
    )
    
    val boxSize by animateDpAsState(
        targetValue = if (animateSize) 150.dp else 100.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "box size"
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "2.4.2 Persistencia y Animaciones",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Divider()
        
        // Sección de Persistencia
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Persistencia con Room Database",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("✅ Base de datos local implementada")
                Text("✅ 2 tablas: users y tasks")
                Text("✅ DAOs con operaciones CRUD")
                Text("✅ Repository Pattern")
                Text("✅ Coroutines para operaciones asíncronas")
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Contador persistente: $counter", fontWeight = FontWeight.Bold)
                    Row {
                        Button(onClick = { counter-- }) {
                            Text("-")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { counter++ }) {
                            Text("+")
                        }
                    }
                }
            }
        }
        
        // Sección de Animaciones
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Animaciones",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Caja animada
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .size(boxSize)
                            .offset(x = boxOffset)
                            .background(boxColor)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Botones de control
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { animateBox = !animateBox },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Mover")
                    }
                    Button(
                        onClick = { animateColor = !animateColor },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Color")
                    }
                    Button(
                        onClick = { animateSize = !animateSize },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Tamaño")
                    }
                }
            }
        }
        
        // Animación de visibilidad
        var showContent by remember { mutableStateOf(true) }
        
        Button(
            onClick = { showContent = !showContent },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (showContent) "Ocultar Contenido" else "Mostrar Contenido")
        }
        
        AnimatedVisibility(
            visible = showContent,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Contenido Animado",
                        fontWeight = FontWeight.Bold
                    )
                    Text("Este contenido aparece y desaparece con animaciones")
                }
            }
        }
    }
}
