package com.example.guaumiau.model

/**
 * Modelo de datos para un Producto del catálogo
 */
data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val category: ProductCategory,
    val price: Double,
    val stock: Int,
    val isEcoFriendly: Boolean = false,
    val isNew: Boolean = false
)

/**
 * Categorías de productos
 */
enum class ProductCategory(val displayName: String, val emoji: String) {
    DOG("Perros", "🐕"),
    CAT("Gatos", "🐈"),
    BIRD("Aves", "🦜"),
    INTERACTIVE("Interactivos", "🎮"),
    ECO("Ecológicos", "♻️")
}

/**
 * Datos de ejemplo para el catálogo
 */
object ProductCatalog {
    val products = listOf(
        Product(
            id = 1,
            name = "Pelota Rebotadora Eco",
            description = "Pelota de material reciclado, resistente y segura para perros de todos los tamaños",
            category = ProductCategory.DOG,
            price = 12990.0,
            stock = 25,
            isEcoFriendly = true,
            isNew = true
        ),
        Product(
            id = 2,
            name = "Ratón Interactivo",
            description = "Juguete con sensor de movimiento que estimula el instinto cazador de tu gato",
            category = ProductCategory.CAT,
            price = 15990.0,
            stock = 18,
            isNew = true
        ),
        Product(
            id = 3,
            name = "Columpio para Pájaros",
            description = "Columpio de madera natural ideal para aves pequeñas y medianas",
            category = ProductCategory.BIRD,
            price = 8990.0,
            stock = 30,
            isEcoFriendly = true
        ),
        Product(
            id = 4,
            name = "Hueso Masticable XL",
            description = "Hueso de goma duradera con textura dental, perfecto para razas grandes",
            category = ProductCategory.DOG,
            price = 9990.0,
            stock = 40
        ),
        Product(
            id = 5,
            name = "Láser Automático",
            description = "Puntero láser automático con temporizador, mantén a tu gato activo",
            category = ProductCategory.INTERACTIVE,
            price = 19990.0,
            stock = 12,
            isNew = true
        ),
        Product(
            id = 6,
            name = "Pluma Colgante Gato",
            description = "Vara con plumas naturales y cascabel, irresistible para gatos",
            category = ProductCategory.CAT,
            price = 6990.0,
            stock = 50
        ),
        Product(
            id = 7,
            name = "Espejo con Cascabel",
            description = "Espejo seguro con cascabel para aves curiosas",
            category = ProductCategory.BIRD,
            price = 5990.0,
            stock = 35
        ),
        Product(
            id = 8,
            name = "Cuerda Dental Eco",
            description = "Cuerda 100% algodón orgánico para juego y limpieza dental",
            category = ProductCategory.DOG,
            price = 7990.0,
            stock = 45,
            isEcoFriendly = true
        ),
        Product(
            id = 9,
            name = "Túnel Plegable",
            description = "Túnel de tela resistente, plegable y con crujido interior",
            category = ProductCategory.CAT,
            price = 16990.0,
            stock = 20
        ),
        Product(
            id = 10,
            name = "Dispensador de Premios",
            description = "Juguete interactivo que libera snacks mientras tu mascota juega",
            category = ProductCategory.INTERACTIVE,
            price = 22990.0,
            stock = 15,
            isNew = true
        ),
        Product(
            id = 11,
            name = "Frisbee Flotante",
            description = "Frisbee de silicona que flota en agua, perfecto para playa o piscina",
            category = ProductCategory.DOG,
            price = 11990.0,
            stock = 28,
            isNew = true
        ),
        Product(
            id = 12,
            name = "Escalera de Bambú",
            description = "Escalera natural de bambú para aves, fácil de instalar",
            category = ProductCategory.BIRD,
            price = 7990.0,
            stock = 22,
            isEcoFriendly = true
        )
    )
    
    /**
     * Obtiene productos por categoría
     */
    fun getProductsByCategory(category: ProductCategory): List<Product> {
        return products.filter { it.category == category }
    }
    
    /**
     * Obtiene solo productos nuevos
     */
    fun getNewProducts(): List<Product> {
        return products.filter { it.isNew }
    }
    
    /**
     * Obtiene solo productos ecológicos
     */
    fun getEcoProducts(): List<Product> {
        return products.filter { it.isEcoFriendly }
    }
}
