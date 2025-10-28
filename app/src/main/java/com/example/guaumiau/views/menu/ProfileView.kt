package com.example.guaumiau.views.menu

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guaumiau.data.PetType
import com.example.guaumiau.data.model.PetEntity
import com.example.guaumiau.viewmodels.ProfileViewModel

/**
 * Vista de Perfil de Usuario
 * Muestra información del usuario y permite cambiar contraseña
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileView(
    viewModel: ProfileViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    // Mostrar mensajes de éxito/error
    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        if (uiState.successMessage != null || uiState.errorMessage != null) {
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Título
        Text(
            text = "👤 Mi Perfil",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.user != null) {
            val user = uiState.user!!

            // Información del Usuario
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Información Personal",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Datos de tu cuenta",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Nombre completo
                    ProfileInfoItem(
                        icon = Icons.Default.Person,
                        label = "Nombre Completo",
                        value = user.fullName
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Email
                    ProfileInfoItem(
                        icon = Icons.Default.Email,
                        label = "Correo Electrónico",
                        value = user.email
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Teléfono
                    ProfileInfoItem(
                        icon = Icons.Default.Phone,
                        label = "Teléfono",
                        value = user.phone ?: "No registrado"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de Seguridad
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = "Seguridad",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Administra tu contraseña",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    // Contraseña
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Contraseña",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "••••••••",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.6f)
                            )
                        }

                        Button(
                            onClick = { viewModel.togglePasswordChangeDialog() },
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Cambiar")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sección de Mascotas
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Mis Mascotas",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "${uiState.pets.size} mascota(s) registrada(s)",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.7f)
                            )
                        }
                        // Botón con animación de pulso
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.15f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(800),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "addPetScale"
                        )
                        
                        IconButton(
                            onClick = { viewModel.toggleAddPetDialog() },
                            modifier = Modifier.scale(scale)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar mascota",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }

                    Divider(modifier = Modifier.padding(vertical = 12.dp))

                    if (uiState.pets.isEmpty()) {
                        // Estado vacío
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🐾",
                                fontSize = 48.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No tienes mascotas registradas",
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Agrega una mascota usando el botón +",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.6f)
                            )
                        }
                    } else {
                        // Lista de mascotas con animación
                        uiState.pets.forEachIndexed { index, pet ->
                            AnimatedPetItem(
                                pet = pet,
                                index = index,
                                isDeleting = uiState.isDeletingPet,
                                onDelete = { viewModel.deletePet(pet.id) }
                            )
                            if (pet != uiState.pets.last()) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

            // Mensajes de éxito/error con animación
            AnimatedVisibility(
                visible = uiState.successMessage != null,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it }
                ) + fadeOut()
            ) {
                uiState.successMessage?.let { message ->
                    Card(
                        modifier = Modifier.padding(top = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = message)
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = uiState.errorMessage != null,
                enter = slideInVertically(
                    initialOffsetY = { -it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy
                    )
                ) + fadeIn(),
                exit = slideOutVertically(
                    targetOffsetY = { -it }
                ) + fadeOut()
            ) {
                uiState.errorMessage?.let { message ->
                    Card(
                        modifier = Modifier.padding(top = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        }

        // Diálogo de cambio de contraseña
        if (uiState.showPasswordChangeDialog) {
            ChangePasswordDialog(
                currentPassword = uiState.currentPassword,
                newPassword = uiState.newPassword,
                confirmNewPassword = uiState.confirmNewPassword,
                currentPasswordError = uiState.currentPasswordError,
                newPasswordError = uiState.newPasswordError,
                confirmNewPasswordError = uiState.confirmNewPasswordError,
                isChangingPassword = uiState.isChangingPassword,
                onCurrentPasswordChange = viewModel::onCurrentPasswordChange,
                onNewPasswordChange = viewModel::onNewPasswordChange,
                onConfirmNewPasswordChange = viewModel::onConfirmNewPasswordChange,
                onConfirm = viewModel::changePassword,
                onDismiss = viewModel::togglePasswordChangeDialog
            )
        }
        
        // Diálogo de agregar mascota
        if (uiState.showAddPetDialog) {
            AddPetDialog(
                viewModel = viewModel,
                uiState = uiState
            )
        }
    }
}

/**
 * Componente reutilizable para mostrar información del perfil
 */
@Composable
private fun ProfileInfoItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
            )
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * Diálogo para cambiar la contraseña
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangePasswordDialog(
    currentPassword: String,
    newPassword: String,
    confirmNewPassword: String,
    currentPasswordError: String?,
    newPasswordError: String?,
    confirmNewPasswordError: String?,
    isChangingPassword: Boolean,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var showCurrentPassword by remember { mutableStateOf(false) }
    var showNewPassword by remember { mutableStateOf(false) }
    var showConfirmPassword by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { if (!isChangingPassword) onDismiss() },
        icon = {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        },
        title = {
            Text(text = "Cambiar Contraseña")
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Contraseña actual
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = onCurrentPasswordChange,
                    label = { Text("Contraseña Actual") },
                    visualTransformation = if (showCurrentPassword) 
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showCurrentPassword = !showCurrentPassword }) {
                            Icon(
                                imageVector = if (showCurrentPassword) 
                                    Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showCurrentPassword) 
                                    "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    isError = currentPasswordError != null,
                    supportingText = currentPasswordError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isChangingPassword
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Nueva contraseña
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = onNewPasswordChange,
                    label = { Text("Nueva Contraseña") },
                    visualTransformation = if (showNewPassword) 
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showNewPassword = !showNewPassword }) {
                            Icon(
                                imageVector = if (showNewPassword) 
                                    Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showNewPassword) 
                                    "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    isError = newPasswordError != null,
                    supportingText = newPasswordError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isChangingPassword
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Confirmar nueva contraseña
                OutlinedTextField(
                    value = confirmNewPassword,
                    onValueChange = onConfirmNewPasswordChange,
                    label = { Text("Confirmar Nueva Contraseña") },
                    visualTransformation = if (showConfirmPassword) 
                        VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                            Icon(
                                imageVector = if (showConfirmPassword) 
                                    Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (showConfirmPassword) 
                                    "Ocultar contraseña" else "Mostrar contraseña"
                            )
                        }
                    },
                    isError = confirmNewPasswordError != null,
                    supportingText = confirmNewPasswordError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { 
                            focusManager.clearFocus()
                            if (!isChangingPassword) onConfirm()
                        }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isChangingPassword
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Información de requisitos
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "La contraseña debe tener:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("• Mínimo 6 caracteres", fontSize = 11.sp)
                        Text("• Al menos una letra mayúscula", fontSize = 11.sp)
                        Text("• Al menos un número", fontSize = 11.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                enabled = !isChangingPassword
            ) {
                if (isChangingPassword) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (isChangingPassword) "Guardando..." else "Guardar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isChangingPassword
            ) {
                Text("Cancelar")
            }
        }
    )
}

/**
 * Item de mascota con animación de entrada
 */
@Composable
private fun AnimatedPetItem(
    pet: PetEntity,
    index: Int,
    isDeleting: Boolean,
    onDelete: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(key1 = pet.id) {
        isVisible = true
    }
    
    // Animación de deslizamiento desde la izquierda
    val offsetX by animateDpAsState(
        targetValue = if (isVisible) 0.dp else (-50).dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "offsetX"
    )
    
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 400,
            delayMillis = index * 100
        ),
        label = "alpha"
    )
    
    Box(
        modifier = Modifier
            .offset(x = offsetX)
            .graphicsLayer(alpha = alpha)
    ) {
        PetItem(
            pet = pet,
            isDeleting = isDeleting,
            onDelete = onDelete
        )
    }
}

/**
 * Item de mascota en la lista
 */
@Composable
private fun PetItem(
    pet: PetEntity,
    isDeleting: Boolean,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono según tipo
            Icon(
                imageVector = when (pet.type) {
                    "PERRO" -> Icons.Default.Person // Usamos Person como placeholder
                    "GATO" -> Icons.Default.Star
                    "AVE" -> Icons.Default.Send
                    else -> Icons.Default.Favorite
                },
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = pet.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = pet.type,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            // Botón eliminar o spinner
            if (isDeleting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar mascota",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    
    // Diálogo de confirmación de eliminación
    if (showDeleteDialog && !isDeleting) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Eliminar Mascota") },
            text = { Text("¿Estás seguro de que deseas eliminar a ${pet.name}?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDelete()
                        showDeleteDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
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
 * Diálogo para agregar mascota
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPetDialog(
    viewModel: ProfileViewModel,
    uiState: com.example.guaumiau.viewmodels.ProfileUiState
) {
    AlertDialog(
        onDismissRequest = { if (!uiState.isAddingPet) viewModel.toggleAddPetDialog() },
        icon = {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        },
        title = { Text("Agregar Mascota") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Nombre de la mascota
                OutlinedTextField(
                    value = uiState.newPetName,
                    onValueChange = viewModel::onNewPetNameChange,
                    label = { Text("Nombre de la mascota") },
                    isError = uiState.newPetNameError != null,
                    supportingText = uiState.newPetNameError?.let { { Text(it) } },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !uiState.isAddingPet,
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Tipo de mascota
                Text(
                    text = "Tipo de mascota:",
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                PetType.values().forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.newPetType == type,
                            onClick = { viewModel.onNewPetTypeChange(type) },
                            enabled = !uiState.isAddingPet
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = type.displayName,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { viewModel.addPet() },
                enabled = !uiState.isAddingPet
            ) {
                if (uiState.isAddingPet) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (uiState.isAddingPet) "Agregando..." else "Agregar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { viewModel.toggleAddPetDialog() },
                enabled = !uiState.isAddingPet
            ) {
                Text("Cancelar")
            }
        }
    )
}
