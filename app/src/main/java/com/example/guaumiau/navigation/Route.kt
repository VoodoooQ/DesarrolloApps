package com.example.guaumiau.navigation

/**
 * Rutas de navegación para el menú lateral
 */
sealed class Route(val route: String) {
    object Home : Route("home")
    object Catalog : Route("catalogo")
    object Profile : Route("perfil")
    object Foro : Route("foro")
    object Option5 : Route("camara")
    object Weather : Route("clima") // Nueva ruta para clima
    object RailwayTest : Route("railway_test") // Prueba de Railway API
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
        route = Route.Weather,
        title = "Clima",
        description = "Clima de Santiago"
    ),
    MenuItem(
        route = Route.RailwayTest,
        title = "Railway API",
        description = "Prueba de microservicio"
    ),
    MenuItem(
        route = Route.Foro,
        title = "Foro",
        description = "Feed de publicaciones"
    ),
    MenuItem(
        route = Route.Option5,
        title = "Crear Publicación",
        description = "Tomar foto y publicar"
    )
)
