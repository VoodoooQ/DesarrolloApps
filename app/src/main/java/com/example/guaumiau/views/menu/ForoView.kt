package com.example.guaumiau.views.menu

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.guaumiau.data.local.AppDatabase
import com.example.guaumiau.data.model.PostEntity
import com.example.guaumiau.data.repository.PostRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * Vista del Foro - Feed de publicaciones de mascotas
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForoView() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    // Inicializar repositorio
    val postRepository = remember {
        val database = AppDatabase.getDatabase(context)
        PostRepository(database.postDao())
    }
    
    // Estado para las publicaciones
    var posts by remember { mutableStateOf<List<PostEntity>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var deletingPostId by remember { mutableStateOf<Int?>(null) }
    
    // Cargar publicaciones al iniciar
    LaunchedEffect(Unit) {
        scope.launch {
            posts = postRepository.getAllPosts()
            isLoading = false
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Foro de Mascotas",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    // Indicador de carga
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                posts.isEmpty() -> {
                    // Mensaje cuando no hay publicaciones
                    EmptyStateView()
                }
                else -> {
                    // Lista de publicaciones
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(posts) { post ->
                            PostCard(
                                post = post,
                                isDeleting = deletingPostId == post.id,
                                onDelete = { postToDelete ->
                                    scope.launch {
                                        deletingPostId = postToDelete.id
                                        // Simular delay para mostrar el loader
                                        kotlinx.coroutines.delay(800)
                                        postRepository.deletePost(postToDelete)
                                        posts = postRepository.getAllPosts()
                                        deletingPostId = null
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Card para mostrar una publicación individual
 */
@Composable
fun PostCard(
    post: PostEntity,
    onDelete: (PostEntity) -> Unit,
    isDeleting: Boolean = false
) {
    var expanded by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
            // Header: Foto de perfil, nombre y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar del usuario
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier.padding(8.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column {
                        Text(
                            text = post.userName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = formatTimestamp(post.timestamp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                // Menú de opciones
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Más opciones"
                    )
                }
                
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Eliminar") },
                        onClick = {
                            expanded = false
                            showDeleteDialog = true
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Descripción
            Text(
                text = post.description,
                style = MaterialTheme.typography.bodyLarge
            )
            
            // Imagen (si existe)
            post.imageUri?.let { uriString ->
                Spacer(modifier = Modifier.height(12.dp))
                
                val imageLoadResult = remember(uriString) {
                    try {
                        val uri = Uri.parse(uriString)
                        val inputStream = context.contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        bitmap
                    } catch (e: Exception) {
                        null
                    }
                }
                
                if (imageLoadResult != null) {
                    Image(
                        bitmap = imageLoadResult.asImageBitmap(),
                        contentDescription = "Foto de la publicación",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Si hay error al cargar la imagen, mostrar placeholder
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.BrokenImage,
                                contentDescription = "Imagen no disponible",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Imagen no disponible",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Acciones: likes, comentarios (placeholder para futuro)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Likes",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${post.likes}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
            
        // Overlay de carga al eliminar
        if (isDeleting) {
            Surface(
                modifier = Modifier.matchParentSize(),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Eliminando...",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
        }
    }
    
    // Diálogo de confirmación de eliminación
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar publicación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta publicación?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(post)
                        showDeleteDialog = false
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Vista cuando no hay publicaciones
 */
@Composable
fun EmptyStateView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Pets,
            contentDescription = "Sin publicaciones",
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "¡No hay publicaciones aún!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Usa la cámara para crear tu primera publicación",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * Formatea el timestamp a formato legible
 */
fun formatTimestamp(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60000 -> "Hace un momento"
        diff < 3600000 -> "${diff / 60000} min"
        diff < 86400000 -> "${diff / 3600000} h"
        diff < 604800000 -> "${diff / 86400000} d"
        else -> {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}
