package com.example.guaumiau.views.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guaumiau.model.Product
import com.example.guaumiau.model.ProductCatalog
import com.example.guaumiau.model.ProductCategory
import java.text.NumberFormat
import java.util.*

/**
 * Vista de Catálogo de Productos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogView() {
    var selectedCategory by remember { mutableStateOf<ProductCategory?>(null) }
    var showOnlyNew by remember { mutableStateOf(false) }
    var showOnlyEco by remember { mutableStateOf(false) }
    
    val filteredProducts = remember(selectedCategory, showOnlyNew, showOnlyEco) {
        var products = ProductCatalog.products
        
        if (selectedCategory != null) {
            products = products.filter { it.category == selectedCategory }
        }
        if (showOnlyNew) {
            products = products.filter { it.isNew }
        }
        if (showOnlyEco) {
            products = products.filter { it.isEcoFriendly }
        }
        
        products
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Encabezado
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.primaryContainer,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "🛍️ Catálogo de Productos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "${filteredProducts.size} productos disponibles",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
        
        // Filtros de categoría
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón "Todos"
            item {
                FilterChip(
                    selected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    label = { Text("Todos") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )
            }
            
            // Categorías
            items(ProductCategory.values()) { category ->
                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { 
                        selectedCategory = if (selectedCategory == category) null else category 
                    },
                    label = { Text(category.displayName) },
                    leadingIcon = {
                        Text(category.emoji, fontSize = 18.sp)
                    }
                )
            }
        }
        
        // Filtros especiales
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = showOnlyNew,
                onClick = { showOnlyNew = !showOnlyNew },
                label = { Text("Novedades") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            )
            
            FilterChip(
                selected = showOnlyEco,
                onClick = { showOnlyEco = !showOnlyEco },
                label = { Text("Ecológicos") },
                leadingIcon = {
                    Text("♻️", fontSize = 18.sp)
                }
            )
        }
        
        Divider(modifier = Modifier.padding(horizontal = 16.dp))
        
        // Lista de productos
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(filteredProducts) { product ->
                ProductCard(product = product)
            }
        }
    }
}

/**
 * Card de producto individual
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Placeholder de imagen
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primaryContainer,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = product.category.emoji,
                    fontSize = 40.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Información del producto
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    // Nombre del producto
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = product.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )
                        
                        if (product.isNew) {
                            Badge(
                                containerColor = MaterialTheme.colorScheme.primary
                            ) {
                                Text("Nuevo", fontSize = 10.sp)
                            }
                        }
                        
                        if (product.isEcoFriendly) {
                            Text("♻️", fontSize = 16.sp)
                        }
                    }
                    
                    // Categoría
                    Text(
                        text = "${product.category.emoji} ${product.category.displayName}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Descripción
                    Text(
                        text = product.description,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 18.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Precio y stock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatPrice(product.price),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (product.stock > 10) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = "Stock: ${product.stock}",
                            fontSize = 12.sp,
                            color = if (product.stock > 10) 
                                MaterialTheme.colorScheme.onSurface 
                            else 
                                MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

/**
 * Formatea el precio en formato chileno
 */
fun formatPrice(price: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    return format.format(price)
}
