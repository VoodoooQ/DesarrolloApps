package com.example.guaumiau.navigation

/**
 * Rutas de navegación para el menú lateral
 */
sealed class Route(val route: String) {
    object Home : Route("home")
    object Catalog : Route("catalogo")
    object Profile : Route("perfil")
    object Option5 : Route("camara")
}

/**
 * Items del menú lateral
 */
data class MenuItem(
    val route: Route,
    val title: String,
    val description: String
)

/**
 * Lista de items del menú
 */
val menuItems = listOf(
    MenuItem(
        route = Route.Home,
        title = "Inicio",
        description = "Pantalla principal"
    ),
    MenuItem(
        route = Route.Catalog,
        title = "Catálogo",
        description = "Ver productos disponibles"
    ),
    MenuItem(
        route = Route.Profile,
        title = "Mi Perfil",
        description = "Ver y editar perfil"
    ),
    MenuItem(
        route = Route.Option5,
        title = "Función Nativa (Cámara)",
        description = "Integración con cámara"
    )
)
