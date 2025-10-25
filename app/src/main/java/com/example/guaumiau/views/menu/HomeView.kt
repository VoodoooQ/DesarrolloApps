package com.example.guaumiau.views.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vista de Home - Pantalla principal para clientes
 */
@Composable
fun HomeView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "🐾 Bienvenido a Guau&Miau",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Divider()
        
        Text(
            text = "Tu Tienda de Juguetes para Mascotas",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Card de bienvenida
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "¡Encuentra el Juguete Perfecto!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Productos innovadores y sostenibles para tus compañeros peludos",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
        
        // Nuestra Misión
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🌱 Nuestra Misión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "En Guau&Miau nos dedicamos a ofrecer los mejores juguetes " +
                            "para tus mascotas, combinando innovación, calidad y " +
                            "responsabilidad ambiental. Cada producto está diseñado " +
                            "pensando en la felicidad y bienestar de tus compañeros peludos.",
                    fontSize = 14.sp
                )
            }
        }
        
        // Por qué elegirnos
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "✨ ¿Por Qué Elegirnos?",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                FeatureItem(
                    icon = Icons.Default.CheckCircle,
                    title = "Innovadores",
                    description = "Diseños únicos y funcionales"
                )
                FeatureItem(
                    icon = Icons.Default.Star,
                    title = "Sostenibles",
                    description = "Materiales ecológicos y reciclables"
                )
                FeatureItem(
                    icon = Icons.Default.Favorite,
                    title = "Seguros",
                    description = "No tóxicos y duraderos"
                )
                FeatureItem(
                    icon = Icons.Default.AccountCircle,
                    title = "Para Todos",
                    description = "Perros, gatos, aves y más"
                )
            }
        }
        
        // Categorías de productos
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🎁 Explora Nuestras Categorías",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Text("🐕 Juguetes para Perros")
                Text("🐈 Juguetes para Gatos")
                Text("🦜 Juguetes para Aves")
                Text("🐾 Accesorios Interactivos")
                Text("♻️ Línea Ecológica")
                Text("🎉 Novedades y Ofertas")
            }
        }
    }
}

@Composable
fun FeatureItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            Text(
                text = description,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
