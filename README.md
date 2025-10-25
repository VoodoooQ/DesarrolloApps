# GuauMiau - Aplicación Android MVVM

## Descripción
GuauMiau es una aplicación Android para la tienda de mascotas **GUAU&MIAU** desarrollada siguiendo el patrón de arquitectura MVVM (Model-View-ViewModel). Incluye sistema completo de autenticación (Login/Registro) con validaciones en tiempo real y gestión de mascotas.

## 🎯 Características Principales

### ✅ Sistema de Autenticación
- **Pantalla de Login** con validación de credenciales
- **Pantalla de Registro** de usuarios con validaciones en tiempo real
- Validación de correos exclusivamente del dominio **@duoc.cl**
- Contraseñas seguras con requisitos estrictos
- Opción "Olvidé mi contraseña"

### ✅ Registro de Mascotas
- Añadir múltiples mascotas durante el registro
- Tipos soportados: PERRO, GATO, AVE, Otro
- Validación de nombres y tipos obligatorios
- Interfaz dinámica para agregar/eliminar mascotas

### 🔑 Credenciales de Prueba
```
Email:     admin@duoc.cl
Password:  Admin123!
```

## Especificaciones Técnicas

### Entorno de Desarrollo
- **Android Studio**: Hedgehog 2023.1.1 Patch 2
- **Kotlin**: 1.9.0
- **JVM Target**: 1.8
- **Gradle Plugin**: 8.2.2
- **Compile SDK**: 34
- **Min SDK**: 24 (Android Nougat)
- **Target SDK**: 34

### Arquitectura MVVM

El proyecto está organizado siguiendo el patrón MVVM con Clean Architecture:

```
com.example.guaumiau/
├── data/                          # Capa de Datos
│   ├── local/                     # Base de datos local
│   │   ├── dao/                   # Data Access Objects
│   │   │   └── PetDao.kt
│   │   ├── entity/                # Entidades de Room
│   │   │   └── PetEntity.kt
│   │   └── AppDatabase.kt
│   ├── remote/                    # API remota
│   │   ├── api/
│   │   │   └── PetApiService.kt
│   │   └── RetrofitClient.kt
│   └── repository/                # Repositorios
│       └── PetRepository.kt
│
├── domain/                        # Capa de Dominio
│   ├── model/                     # Modelos de negocio
│   │   └── Pet.kt
│   └── usecase/                   # Casos de uso
│       ├── GetPetsUseCase.kt
│       └── AddPetUseCase.kt
│
├── presentation/                  # Capa de Presentación
│   ├── adapter/                   # Adaptadores de RecyclerView
│   │   └── PetAdapter.kt
│   └── viewmodel/                 # ViewModels
│       ├── PetViewModel.kt
│       └── PetViewModelFactory.kt
│
├── di/                           # Inyección de Dependencias
│   └── AppModule.kt
│
├── util/                         # Utilidades
│   ├── Resource.kt
│   ├── Constants.kt
│   └── ImageUtils.kt
│
├── ui/                           # Temas de UI (Compose)
│   └── theme/
│
└── MainActivity.kt               # Actividad principal
```

## Dependencias Principales

### Core Android
- `androidx.core:core-ktx:1.10.1`
- `androidx.appcompat:appcompat:1.6.1`
- `material:1.9.0`

### Lifecycle Components
- `lifecycle-runtime-ktx:2.6.1`
- `lifecycle-viewmodel-ktx:2.6.1`
- `lifecycle-livedata-ktx:2.6.1`

### Navigation
- `navigation-fragment-ktx:2.6.0`
- `navigation-ui-ktx:2.6.0`

### Room Database
- `room-runtime:2.5.2`
- `room-ktx:2.5.2`
- `room-compiler:2.5.2` (kapt)

### Networking
- `retrofit:2.9.0`
- `converter-gson:2.9.0`
- `logging-interceptor:4.11.0`

### Image Loading
- `glide:4.15.1`

### Coroutines
- `kotlinx-coroutines-android:1.7.1`

## Características

### ✅ Implementado
- ✅ Estructura de carpetas MVVM completa
- ✅ Base de datos local con Room
- ✅ Capa de datos con Repository Pattern
- ✅ ViewModels con StateFlow y LiveData
- ✅ Casos de uso (Use Cases)
- ✅ Inyección de dependencias manual
- ✅ Adaptador de RecyclerView con DiffUtil
- ✅ Layouts XML para UI
- ✅ Manejo de estados con Resource class
- ✅ Configuración de Retrofit para API
- ✅ Utilidades para carga de imágenes

### 📋 Pendiente (Para extender)
- Implementar más pantallas (Detalle, Edición, Agregar)
- Conectar con API real
- Agregar Navigation Component
- Implementar búsqueda y filtros avanzados
- Tests unitarios e instrumentados
- Manejo de errores mejorado

## Uso

### Agregar una Mascota
```kotlin
val pet = Pet(
    id = 0,
    name = "Firulais",
    type = "perro",
    breed = "Labrador",
    age = 3,
    imageUrl = null,
    description = "Un perro muy amigable"
)
viewModel.addPet(pet)
```

### Observar Lista de Mascotas
```kotlin
lifecycleScope.launch {
    viewModel.pets.collect { pets ->
        // Actualizar UI
        petAdapter.submitList(pets)
    }
}
```

## Notas Importantes

⚠️ **No modificar las siguientes configuraciones:**
- Versión de Kotlin (1.9.0)
- JVM Target (1.8)
- Versiones de compilación y target SDK
- Gradle Plugin version

Estas configuraciones están optimizadas para Android Studio Hedgehog 2023.1.1 Patch 2 y API 24.

## Compilación

```bash
# Limpiar y compilar
./gradlew clean build

# Compilar APK de debug
./gradlew assembleDebug

# Instalar en dispositivo conectado
./gradlew installDebug
```

## Licencia
Este proyecto es de uso educativo.
