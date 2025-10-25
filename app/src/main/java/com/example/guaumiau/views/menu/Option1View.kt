package com.example.guaumiau.views.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vista de Componentes (Opción 1)
 */
@Composable
fun Option1View() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "2.1.3 Componentes",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Divider()
        
        Text(
            text = "Ejemplos de Componentes Material Design 3",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Ejemplos de componentes
        Button(onClick = { }) {
            Text("Button")
        }
        
        OutlinedButton(onClick = { }) {
            Text("Outlined Button")
        }
        
        FilledTonalButton(onClick = { }) {
            Text("Filled Tonal Button")
        }
        
        var switchState by remember { mutableStateOf(false) }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Switch", modifier = Modifier.weight(1f))
            Switch(checked = switchState, onCheckedChange = { switchState = it })
        }
        
        var sliderValue by remember { mutableStateOf(0.5f) }
        Column {
            Text("Slider: ${(sliderValue * 100).toInt()}%")
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it }
            )
        }
        
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = sliderValue
        )
    }
}
