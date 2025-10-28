package com.example.guaumiau.views.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guaumiau.data.local.AppDatabase
import com.example.guaumiau.data.repository.PetRepository
import com.example.guaumiau.data.repository.UserRepository
import com.example.guaumiau.navigation.Route
import com.example.guaumiau.navigation.menuItems
import com.example.guaumiau.viewmodels.ProfileViewModel
import com.example.guaumiau.viewmodels.ProfileViewModelFactory
import kotlinx.coroutines.launch

/**
 * MenuShellView - Vista principal con menú lateral (Drawer)
 * Contiene el ModalNavigationDrawer y el sistema de navegación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuShellView(
    userEmail: String = "",
    onBackToLogin: () -> Unit = {}
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentRoute by remember { mutableStateOf(Route.Home.route) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isLoggingOut by remember { mutableStateOf(false) }
    
    // Inicializar repositorio
    val database = remember { AppDatabase.getDatabase(context) }
    val userRepository = remember { UserRepository(database.userDao()) }
    val petRepository = remember { PetRepository(database.petDao()) }
    
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerContent(
                    currentRoute = currentRoute,
                    onMenuItemClick = { route ->
                        currentRoute = route.route
                        navController.navigate(route.route) {
                            // Evitar múltiples copias en el back stack
                            popUpTo(Route.Home.route) { inclusive = false }
                            launchSingleTop = true
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    onBackToLogin = {
                        scope.launch {
                            drawerState.close()
                        }
                        showLogoutDialog = true
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "🐾 Guau&Miau - Juguetes para Mascotas",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Abrir menú"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Route.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Route.Home.route) { HomeView() }
                composable(Route.Catalog.route) { CatalogView() }
                composable(Route.Profile.route) {
                    // Crear ProfileViewModel con el email del usuario
                    val profileViewModel: ProfileViewModel = viewModel(
                        factory = ProfileViewModelFactory(userRepository, petRepository, userEmail)
                    )
                    ProfileView(viewModel = profileViewModel)
                }
                composable(Route.Option5.route) { Option5CameraView() }
            }
        }
    }
    
    // Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { if (!isLoggingOut) showLogoutDialog = false },
            icon = {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                Button(
                    onClick = {
                        isLoggingOut = true
                        scope.launch {
                            // Simular delay para mostrar loader
                            kotlinx.coroutines.delay(600)
                            isLoggingOut = false
                            showLogoutDialog = false
                            onBackToLogin()
                        }
                    },
                    enabled = !isLoggingOut,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    if (isLoggingOut) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onError
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (isLoggingOut) "Cerrando..." else "Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false },
                    enabled = !isLoggingOut
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

/**
 * Contenido del Drawer (menú lateral)
 */
@Composable
fun DrawerContent(
    currentRoute: String,
    onMenuItemClick: (Route) -> Unit,
    onBackToLogin: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header del drawer
        Text(
            text = "GuauMiau",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        
        Text(
            text = "Menú de Navegación",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Divider()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Items del menú
        menuItems.forEach { menuItem ->
            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = getIconForRoute(menuItem.route),
                        contentDescription = null
                    )
                },
                label = {
                    Column {
                        Text(
                            text = menuItem.title,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = menuItem.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                selected = currentRoute == menuItem.route.route,
                onClick = { onMenuItemClick(menuItem.route) },
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Divider()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Botón de cerrar sesión
        NavigationDrawerItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.ExitToApp,
                    contentDescription = null
                )
            },
            label = { Text("Cerrar Sesión") },
            selected = false,
            onClick = onBackToLogin,
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                unselectedTextColor = MaterialTheme.colorScheme.onErrorContainer,
                unselectedIconColor = MaterialTheme.colorScheme.onErrorContainer
            )
        )
    }
}

/**
 * Obtiene el ícono correspondiente para cada ruta
 */
@Composable
fun getIconForRoute(route: Route): androidx.compose.ui.graphics.vector.ImageVector {
    return when (route) {
        is Route.Home -> Icons.Default.Home
        is Route.Catalog -> Icons.Default.ShoppingCart
        is Route.Profile -> Icons.Default.Person
        is Route.Option5 -> Icons.Default.AccountBox
    }
}
