package com.example.guaumiau.views.menu

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Vista de Función Nativa - Cámara (Opción 5)
 * Implementa captura de foto y selección desde galería
 */
@Composable
fun Option5CameraView() {
    val context = LocalContext.current
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasCameraPermission by remember { mutableStateOf(false) }
    
    // Launcher para tomar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        bitmap?.let {
            imageBitmap = it
            saveImageToStorage(context, it)
            Toast.makeText(context, "📷 Foto capturada correctamente", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Launcher para seleccionar de galería
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            imageBitmap = loadBitmapFromUri(context, it)
            Toast.makeText(context, "🖼️ Imagen seleccionada", Toast.LENGTH_SHORT).show()
        }
    }
    
    // Launcher para permisos de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
        if (isGranted) {
            takePictureLauncher.launch(null)
        } else {
            Toast.makeText(context, "⚠️ Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "📷 Función Nativa - Cámara",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Divider()
        
        Text(
            text = "Comparte Fotos de tu Mascota",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        // Vista previa de imagen
        if (imageBitmap != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        bitmap = imageBitmap!!.asImageBitmap(),
                        contentDescription = "Imagen capturada",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            
            Text(
                text = "✅ Imagen guardada en Pictures/GuauMiau",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "📸 No hay imagen",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Botones de acción
        Button(
            onClick = {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("📷 Tomar Foto de tu Mascota", fontSize = 16.sp)
        }
        
        OutlinedButton(
            onClick = {
                pickImageLauncher.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("🖼️ Seleccionar de Galería", fontSize = 16.sp)
        }
        
        if (imageBitmap != null) {
            FilledTonalButton(
                onClick = {
                    imageBitmap = null
                    imageUri = null
                    Toast.makeText(context, "🗑️ Imagen eliminada", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🗑️ Limpiar Imagen", fontSize = 16.sp)
            }
        }
        
        // Información de funcionalidad
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "✨ Funcionalidades",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("✅ Captura de foto con cámara nativa")
                Text("✅ Selección de imagen desde galería")
                Text("✅ Vista previa de imagen en tiempo real")
                Text("✅ Guardado automático en almacenamiento")
                Text("✅ Gestión de permisos en tiempo de ejecución")
            }
        }
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ℹ️ Comparte Momentos Especiales",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Captura y guarda fotos de tu mascota disfrutando de " +
                            "sus juguetes favoritos de Guau&Miau. Comparte esos " +
                            "momentos especiales con nosotros en redes sociales. " +
                            "Las imágenes se guardan en la carpeta Pictures/GuauMiau del dispositivo.",
                    fontSize = 14.sp
                )
            }
        }
    }
}

/**
 * Guarda el bitmap en el almacenamiento externo
 */
private fun saveImageToStorage(context: Context, bitmap: Bitmap) {
    try {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "GuauMiau_$timestamp.jpg"
        
        val picturesDir = File(
            context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES),
            "GuauMiau"
        )
        
        if (!picturesDir.exists()) {
            picturesDir.mkdirs()
        }
        
        val imageFile = File(picturesDir, fileName)
        val outputStream = FileOutputStream(imageFile)
        
        bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
        outputStream.flush()
        outputStream.close()
        
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Carga un bitmap desde un URI
 */
private fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
