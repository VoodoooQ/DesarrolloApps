package com.example.guaumiau.views.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Vista de Formularios (Opción 3)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Option3View() {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var acceptTerms by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("Opción 1") }
    var expanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "2.3.3 Formularios",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Divider()
        
        Text(
            text = "Ejemplo de Formulario Completo",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Campo de nombre
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre completo") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        // Campo de email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )
        
        // Campo de teléfono
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true
        )
        
        // Dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedOption,
                onValueChange = { },
                readOnly = true,
                label = { Text("Selecciona una opción") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Opción 1") },
                    onClick = {
                        selectedOption = "Opción 1"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Opción 2") },
                    onClick = {
                        selectedOption = "Opción 2"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Opción 3") },
                    onClick = {
                        selectedOption = "Opción 3"
                        expanded = false
                    }
                )
            }
        }
        
        // Campo de mensaje
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            label = { Text("Mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            maxLines = 5
        )
        
        // Checkbox
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = acceptTerms,
                onCheckedChange = { acceptTerms = it }
            )
            Text(
                text = "Acepto los términos y condiciones",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Botón de envío
        Button(
            onClick = { /* Acción de envío */ },
            modifier = Modifier.fillMaxWidth(),
            enabled = name.isNotBlank() && email.isNotBlank() && acceptTerms
        ) {
            Text("Enviar Formulario")
        }
    }
}
