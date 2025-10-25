package com.example.guaumiau.views.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vista de Navegación (Opción 2)
 */
@Composable
fun Option2View() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "2.2.4 Navegación",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Divider()
        
        Text(
            text = "Sistema de Navegación",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Navegación Implementada",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("✅ Navigation Component")
                Text("✅ ModalNavigationDrawer")
                Text("✅ NavHost")
                Text("✅ Rutas definidas")
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Flujo de Navegación",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("1. MainActivity → Login/Registro")
                Text("2. Login → TaskListActivity")
                Text("3. Menu Lateral → Diferentes vistas")
                Text("4. Back Navigation configurada")
            }
        }
    }
}
