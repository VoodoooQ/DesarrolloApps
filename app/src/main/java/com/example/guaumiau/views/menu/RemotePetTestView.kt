package com.example.guaumiau.views.menu

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guaumiau.data.model.PetEntity
import com.example.guaumiau.model.PetType
import com.example.guaumiau.viewmodels.RemotePetViewModel

/**
 * Pantalla de prueba para validar integración con Railway API
 * 
 * Funcionalidades:
 * - Sincronizar mascotas desde Railway
 * - Crear nuevas mascotas (POST)
 * - Eliminar mascotas (DELETE)
 * - Ver logs de errores/éxitos
 * - Indicadores de carga
 * 
 * Compatible con Android Studio Iguana 2023.1.1 Patch 2
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RemotePetTestView(
    viewModel: RemotePetViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Mostrar Snackbar de mensajes
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
            viewModel.clearSuccessMessage()
        }
    }
    
    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Long
            )
            viewModel.clearErrorMessage()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Railway API - Prueba")
                        Text(
                            text = "Microservicio de Mascotas",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Botón de sincronización
                    IconButton(
                        onClick = { viewModel.syncPetsFromRemote() },
                        enabled = !uiState.isSyncing
                    ) {
                        if (uiState.isSyncing) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Sincronizar con Railway"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.showAddPetDialog() },
                icon = { Icon(Icons.Default.Add, "Agregar") },
                text = { Text("Crear Mascota") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header con info de API
            ApiInfoCard()
            
            Divider()
            
            // Lista de mascotas
            if (uiState.isLoading && uiState.pets.isEmpty()) {
                LoadingContent()
            } else if (uiState.pets.isEmpty()) {
                EmptyContent()
            } else {
                PetListContent(
                    pets = uiState.pets,
                    onDeleteClick = { viewModel.showDeleteConfirmation(it) }
                )
            }
        }
    }
    
    // Diálogo para agregar mascota
    if (uiState.showAddPetDialog) {
        AddPetDialog(
            petName = uiState.newPetName,
            petType = uiState.newPetType,
            nameError = uiState.newPetNameError,
            isLoading = uiState.isAddingPet,
            onNameChange = { viewModel.onNewPetNameChange(it) },
            onTypeChange = { viewModel.onNewPetTypeChange(it) },
            onConfirm = { viewModel.createPet() },
            onDismiss = { viewModel.dismissAddPetDialog() }
        )
    }
    
    // Diálogo de confirmación de eliminación
    if (uiState.showDeleteDialog) {
        DeleteConfirmationDialog(
            petName = uiState.petToDelete?.name ?: "",
            isDeleting = uiState.isDeletingPet,
            onConfirm = { viewModel.confirmDeletePet() },
            onDismiss = { viewModel.dismissDeleteDialog() }
        )
    }
}

/**
 * Card con información de la API
 */
@Composable
private fun ApiInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Cloud,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Railway API Status",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = "🔗 https://microservicedm-production.up.railway.app",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(Modifier.height(4.dp))
            
            Text(
                text = "📡 Endpoints: GET, POST, PUT, DELETE /api/pets",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

/**
 * Contenido de carga
 */
@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text("Cargando mascotas...")
        }
    }
}

/**
 * Contenido vacío
 */
@Composable
private fun EmptyContent() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Pets,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.outline
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "No hay mascotas registradas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Presiona + para crear una nueva mascota en Railway",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Lista de mascotas
 */
@Composable
private fun PetListContent(
    pets: List<PetEntity>,
    onDeleteClick: (PetEntity) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Text(
                text = "Mascotas sincronizadas (${pets.size})",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        items(pets, key = { it.id }) { pet ->
            PetCard(
                pet = pet,
                onDeleteClick = { onDeleteClick(pet) }
            )
        }
    }
}

/**
 * Card individual de mascota
 */
@Composable
private fun PetCard(
    pet: PetEntity,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "Tipo: ${pet.type}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "ID: ${pet.id}",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }
            
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

/**
 * Diálogo para agregar mascota
 */
@Composable
private fun AddPetDialog(
    petName: String,
    petType: PetType?,
    nameError: String?,
    isLoading: Boolean,
    onNameChange: (String) -> Unit,
    onTypeChange: (PetType) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Crear Mascota en Railway") },
        text = {
            Column {
                OutlinedTextField(
                    value = petName,
                    onValueChange = onNameChange,
                    label = { Text("Nombre") },
                    isError = nameError != null,
                    supportingText = nameError?.let { { Text(it) } },
                    enabled = !isLoading,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(Modifier.height(16.dp))
                
                Text("Tipo de mascota:", fontWeight = FontWeight.Medium)
                
                Spacer(Modifier.height(8.dp))
                
                PetType.values().forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = petType == type,
                            onClick = { onTypeChange(type) },
                            enabled = !isLoading
                        )
                        Text(type.displayName)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Crear")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Diálogo de confirmación de eliminación
 */
@Composable
private fun DeleteConfirmationDialog(
    petName: String,
    isDeleting: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isDeleting) onDismiss() },
        icon = { Icon(Icons.Default.Warning, null) },
        title = { Text("Eliminar Mascota") },
        text = { Text("¿Estás seguro de eliminar a '$petName'? Esta acción eliminará la mascota del servidor Railway.") },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isDeleting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                if (isDeleting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Eliminar")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isDeleting
            ) {
                Text("Cancelar")
            }
        }
    )
}
