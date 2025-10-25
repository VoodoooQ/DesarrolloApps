package com.example.guaumiau.views.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.guaumiau.navigation.Route
import com.example.guaumiau.navigation.menuItems
import kotlinx.coroutines.launch

/**
 * MenuShellView - Vista principal con menú lateral (Drawer)
 * Contiene el ModalNavigationDrawer y el sistema de navegación
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuShellView(
    onBackToLogin: () -> Unit = {}
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentRoute by remember { mutableStateOf(Route.Home.route) }
    
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
                        onBackToLogin()
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
                composable(Route.Option1.route) { Option1View() }
                composable(Route.Option2.route) { Option2View() }
                composable(Route.Option3.route) { Option3View() }
                composable(Route.Option4.route) { Option4View() }
                composable(Route.Option5.route) { Option5CameraView() }
            }
        }
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
        is Route.Option1 -> Icons.Default.Build
        is Route.Option2 -> Icons.Default.Share
        is Route.Option3 -> Icons.Default.Edit
        is Route.Option4 -> Icons.Default.Star
        is Route.Option5 -> Icons.Default.AccountBox
    }
}
