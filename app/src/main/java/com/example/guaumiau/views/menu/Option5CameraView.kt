package com.example.guaumiau.views.menu

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.guaumiau.data.local.AppDatabase
import com.example.guaumiau.data.model.PostEntity
import com.example.guaumiau.data.repository.PostRepository
import com.example.guaumiau.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * Vista de Función Nativa - Cámara
 * Permite tomar fotos y crear publicaciones para el foro
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Option5CameraView(userEmail: String = "usuario@duoc.cl") {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var description by remember { mutableStateOf("") }
    var isPublishing by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    
    // Repositorios
    val postRepository = remember {
        val database = AppDatabase.getDatabase(context)
        PostRepository(database.postDao())
    }
    
    val userRepository = remember {
        val database = AppDatabase.getDatabase(context)
        UserRepository(database.userDao())
    }
    
    // Obtener nombre de usuario
    var userName by remember { mutableStateOf("Usuario") }
    LaunchedEffect(userEmail) {
        scope.launch {
            val user = userRepository.getUserByEmail(userEmail)
            userName = user?.fullName ?: "Usuario"
        }
    }
    
    // Launcher simple para tomar foto (TakePicturePreview - más estable)
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        try {
            Log.d("CAMERA", "Resultado de cámara recibido: ${bitmap != null}")
            if (bitmap != null) {
                Log.d("CAMERA", "Bitmap válido, guardando...")
                imageBitmap = bitmap
                // Guardar y obtener URI de forma síncrona
                val savedUri = saveImageToStorageAndGetUri(context, bitmap)
                imageUri = savedUri
                Log.d("CAMERA", "Imagen guardada en: $savedUri")
                Toast.makeText(context, "📷 Foto capturada", Toast.LENGTH_SHORT).show()
            } else {
                Log.w("CAMERA", "Bitmap nulo, foto no capturada")
                Toast.makeText(context, "No se pudo capturar la foto", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("CAMERA", "Error en takePictureLauncher", e)
            e.printStackTrace()
            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
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
    
    // Launcher para pedir permiso de cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        try {
            Log.d("CAMERA", "Permiso de cámara: $isGranted")
            if (isGranted) {
                Log.d("CAMERA", "Lanzando cámara...")
                takePictureLauncher.launch(null)
            } else {
                Log.w("CAMERA", "Permiso denegado")
                Toast.makeText(context, "⚠️ Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("CAMERA", "Error al lanzar cámara", e)
            e.printStackTrace()
            Toast.makeText(context, "Error al abrir cámara: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crear Publicación") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Título
            Text(
                text = "📷 Comparte una foto de tu mascota",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            
            // Botones de captura
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        try {
                            Log.d("CAMERA", "Botón cámara presionado")
                            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        } catch (e: Exception) {
                            Log.e("CAMERA", "Error al presionar botón cámara", e)
                            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tomar Foto")
                }
                
                OutlinedButton(
                    onClick = {
                        pickImageLauncher.launch("image/*")
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Galería")
                }
            }
            
            // Vista previa de imagen
            if (imageBitmap != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box {
                        Image(
                            bitmap = imageBitmap!!.asImageBitmap(),
                            contentDescription = "Foto seleccionada",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                                .clip(RoundedCornerShape(12.dp)),
                            contentScale = ContentScale.Crop
                        )
                        
                        // Botón para eliminar la foto (esquina superior derecha)
                        IconButton(
                            onClick = {
                                imageBitmap = null
                                imageUri = null
                                description = ""
                                Toast.makeText(context, "🗑️ Foto eliminada", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar foto",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                        shape = RoundedCornerShape(50)
                                    )
                                    .padding(4.dp)
                            )
                        }
                    }
                }
                
                // Campo de descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    placeholder = { Text("Escribe algo sobre esta foto...") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    leadingIcon = {
                        Icon(Icons.Default.Edit, contentDescription = null)
                    }
                )
                
                // Botón de publicar
                Button(
                    onClick = {
                        if (description.isNotBlank() && imageUri != null) {
                            scope.launch {
                                isPublishing = true
                                try {
                                    // Simular delay para mostrar el loader
                                    kotlinx.coroutines.delay(800)
                                    
                                    val newPost = PostEntity(
                                        userEmail = userEmail,
                                        userName = userName,
                                        description = description,
                                        imageUri = imageUri.toString(),
                                        timestamp = System.currentTimeMillis(),
                                        likes = 0
                                    )
                                    postRepository.insertPost(newPost)
                                    
                                    // Limpiar formulario
                                    imageBitmap = null
                                    imageUri = null
                                    description = ""
                                    showSuccessDialog = true
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        context,
                                        "Error al publicar: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } finally {
                                    isPublishing = false
                                }
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "⚠️ Agrega una descripción para publicar",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isPublishing && description.isNotBlank() && imageUri != null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (isPublishing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publicando...")
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Publicar en el Foro")
                    }
                }
            } else {
                // Instrucciones cuando no hay imagen
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Toma una foto o selecciona una de la galería",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
    
    // Diálogo de éxito
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            },
            title = { Text("¡Publicación creada!") },
            text = { Text("Tu publicación ha sido compartida en el foro exitosamente.") },
            confirmButton = {
                TextButton(onClick = { showSuccessDialog = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
}

/**
 * Crea un archivo temporal para almacenar la foto
 */
private fun createImageFile(context: Context): File {
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timestamp}_",
        ".jpg",
        storageDir
    )
}

/**
 * Guarda el bitmap en el almacenamiento externo y retorna el URI
 */
private fun saveImageToStorageAndGetUri(context: Context, bitmap: Bitmap): Uri? {
    return try {
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
        
        Uri.fromFile(imageFile)
    } catch (e: Exception) {
        e.printStackTrace()
        null
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
