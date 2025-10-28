# 🐾 GuauMiau - App Android

**Aplicación móvil para clientes de GuauMiau**, tienda de juguetes innovadores y sostenibles para mascotas. Desarrollada con MVVM, Jetpack Compose y Room Database.

---

## ✨ Funcionalidades

### 🔐 Autenticación
- Login y Registro con validación @duoc.cl
- **Guardado de mascotas al registrarse** (nombre y tipo)
- Persistencia en Room Database
- Contraseñas validadas (6+ chars, mayúscula, número)
- **Animaciones de entrada** (fadeIn + slideIn)
- **⏳ Loader durante inicio de sesión** (spinner + texto)
- **🚪 Confirmación de cierre de sesión** con loader

### 👤 Perfil de Usuario
- Visualizar datos personales (nombre, email, teléfono)
- **Gestionar mascotas registradas** (ver, agregar, eliminar)
- Cambiar contraseña con validación
- Actualización en tiempo real
- **Animaciones de lista** (deslizamiento + fade)
- **Botón animado con pulso** para agregar mascotas
- **Mensajes animados** de éxito/error
- **⏳ Loaders/Spinners** durante cambio de contraseña y eliminación de mascotas

### 📝 Gestión de Tareas (CRUD)
- Crear, leer, actualizar, eliminar tareas
- Marcar como completadas
- Filtrado por usuario

### 📷 Función Nativa
- Captura de fotos con cámara
- Selección desde galería
- Guardado en almacenamiento local

### 🛍️ Catálogo
- **Filtros interactivos** por categoría (Perros, Gatos, Aves, Otro)
- **Filtros especiales** (Novedades, Ecológicos)
- **Animaciones de aparición** de productos (escala + alpha escalonado)
- Diseño con gradientes
- LazyColumn con cards

### 🎨 Menú de Navegación
1. **Inicio** - Bienvenida y catálogo
2. **Catálogo** - Productos disponibles
3. **Mi Perfil** - Ver y editar perfil
4. **Componentes** - Ejemplos UI Material Design 3
5. **Navegación** - Sistema de rutas
6. **Formularios** - Validaciones en tiempo real
7. **Persistencia** - Room DB y animaciones
8. **Cámara** - Integración nativa

---

## 🛠️ Especificaciones Técnicas

| Tecnología | Versión |
|-----------|---------|
| **Android Studio** | Hedgehog 2023.1.1 Patch 2 |
| **Kotlin** | 1.9.0 |
| **Gradle Plugin** | 8.2.2 |
| **Min SDK** | 24 (Android 7.0) |
| **Target SDK** | 34 (Android 14) |
| **Room** | 2.5.2 |
| **Compose BOM** | 2023.08.00 |

### Arquitectura
```
MVVM + Repository Pattern
├── Data Layer (Room, DAOs, Entities)
├── Domain Layer (Repository, Models, Validator)
└── Presentation Layer (ViewModels, Views, Activities)
```

### Base de Datos Room
**Tabla users:**
- id, fullName, email, password, phone

**Tabla pets:**
- id, name, type, userEmail

**Tabla tasks:**
- id, title, description, date, userId, isCompleted

---

## 🚀 Instalación

```powershell
# Clonar repositorio
git clone <repository-url>

# Compilar
cd DesarrolloApps
.\gradlew.bat assembleDebug --no-daemon

# Ejecutar en Android Studio
Run > Run 'app'
```

---

## 📱 Uso

1. **Registro:** Email @duoc.cl + contraseña segura
2. **Login:** Autenticación con Room DB
3. **Perfil:** Ver datos y cambiar contraseña
4. **Tareas:** CRUD completo con persistencia
5. **Cámara:** Capturar y guardar fotos

---

## 🔒 Seguridad

⚠️ **Solo desarrollo:** Contraseñas sin hash. En producción usar BCrypt/Argon2.

---

## 📋 Implementaciones Recientes

### Perfil de Usuario (27-Oct-2025)
**Archivos creados:**
- `ProfileViewModel.kt` - Lógica de perfil y mascotas
- `ProfileViewModelFactory.kt` - Factory con DI
- `ProfileView.kt` - UI con Material Design 3
- `PetEntity.kt` - Entidad de mascotas
- `PetDao.kt` - DAO de mascotas
- `PetRepository.kt` - Repositorio de mascotas

**Archivos modificados:**
- `UserDao.kt` - Método `updatePassword()`
- `UserRepository.kt` - Método `updatePassword()`
- `AppDatabase.kt` - Versión 3, tabla `pets`
- `Route.kt` - Ruta `Profile`
- `MenuShellView.kt` - Navegación a perfil con PetRepository
- `MenuActivity.kt` - Recibe `userEmail`
- `MainActivity.kt` - Pasa `userEmail` al menú y `PetRepository`
- `RegisterViewModel.kt` - **Guarda mascotas en DB al registrar**

**Características:**
- ✅ Muestra nombre, email, teléfono
- ✅ **Lista de mascotas registradas** (incluyendo las del registro inicial)
- ✅ **Agregar nuevas mascotas** (PERRO, GATO, AVE, OTRO)
- ✅ **Eliminar mascotas con confirmación**
- ✅ Cambio de contraseña con validación
- ✅ Diálogos Material Design 3
- ✅ Mensajes de éxito/error
- ✅ Compatible API 24
- ✅ Persistencia en Room Database
- ✅ **Mascotas guardadas automáticamente al registrarse**
- ✅ **Animaciones implementadas:**
  - Aparición de productos en catálogo (scale + alpha escalonado)
  - Lista de mascotas (slideIn + fade)
  - Mensajes de éxito/error (slideInVertically + fadeIn/Out)
  - Botón agregar mascota (pulso infinito)
  - Login con fade de entrada

---

## 📊 Estado del Proyecto

✅ Autenticación completa  
✅ CRUD de tareas  
✅ Perfil de usuario  
✅ Navegación con Drawer  
✅ Cámara nativa  
✅ Room Database  
✅ Validaciones en tiempo real  
✅ Material Design 3  
✅ **Animaciones Compose** (fadeIn, slideIn, scale, pulso infinito)  

---

## 🐛 Troubleshooting

```powershell
# Limpiar y reconstruir
.\gradlew.bat clean build --no-daemon

# Verificar base de datos
# Los datos se guardan en /data/data/com.example.guaumiau/databases/
```

---

## 👨‍💻 Desarrollo

**Compilación:**
```powershell
.\gradlew.bat assembleDebug --no-daemon    # Debug APK
.\gradlew.bat assembleRelease --no-daemon  # Release APK
.\gradlew.bat test                         # Ejecutar tests
```

---

**Desarrollado para DUOC UC** | *Versión 1.0 - Octubre 2025*
