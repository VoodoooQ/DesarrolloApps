package com.example.guaumiau.navigation

/**
 * Rutas de navegación para el menú lateral
 */
sealed class Route(val route: String) {
    object Home : Route("home")
    object Option1 : Route("componentes")
    object Option2 : Route("navegacion")
    object Option3 : Route("form")
    object Option4 : Route("persistencia_animaciones")
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
        route = Route.Option1,
        title = "2.1.3 Componentes",
        description = "Ejemplos de componentes UI"
    ),
    MenuItem(
        route = Route.Option2,
        title = "2.2.4 Navegación",
        description = "Navegación entre pantallas"
    ),
    MenuItem(
        route = Route.Option3,
        title = "2.3.3 Form",
        description = "Formularios y validaciones"
    ),
    MenuItem(
        route = Route.Option4,
        title = "2.4.2 Persistencia y Animaciones",
        description = "Room DB y animaciones"
    ),
    MenuItem(
        route = Route.Option5,
        title = "2.4.4 Función Nativa (Cámara)",
        description = "Integración con cámara"
    )
)
