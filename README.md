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
4. **🌤️ Clima** - Consulta clima de Santiago (Open-Meteo API)
5. **Foro** - Feed de publicaciones
6. **Cámara** - Captura de fotos de mascotas

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
| **Retrofit** | 2.9.0 |
| **OkHttp** | 4.11.0 |
| **Gson** | 2.10.1 |

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
✅ **🌤️ Consulta de Clima con API externa** (Open-Meteo)  

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

---

## 🌤️ Funcionalidad de Clima (Noviembre 2025)

### Descripción
Consulta del clima actual de Santiago de Chile mediante **Open-Meteo API** (API gratuita sin necesidad de API key). Muestra temperatura, condición climática y un mensaje personalizado según el clima para salir con mascotas.

### Características Implementadas

#### 🎯 Consumo de API
- **API:** Open-Meteo (https://open-meteo.com/)
- **Ubicación:** Santiago, Chile (lat: -33.46, lon: -70.65)
- **Tecnología:** Retrofit 2.9.0 + OkHttp 4.11.0 + Gson 2.10.1
- **Endpoints:** `/v1/forecast` con parámetro `current_weather=true`

#### 🎨 UI/UX
- **Pantalla completa** con Material Design 3
- **Animaciones:**
  - Fade in + Slide in para encabezado
  - Scale + Fade para datos del clima
  - Rotación infinita del emoji de clima
  - Pulso infinito en loading
- **Estados manejados:**
  - ⏳ Loading (spinner animado)
  - ✅ Success (datos completos)
  - ❌ Error (con botón de reintentar)
  - 🔄 Initial (estado previo a carga)
- **Tarjetas (Cards):**
  1. **Principal:** Temperatura, emoji, condición, categoría térmica
  2. **Mensaje:** Recomendación personalizada con ícono de mascota
  3. **Detalles:** Velocidad/dirección del viento, hora de actualización
- **Colores dinámicos** según condición (azul para soleado, gris para nublado, etc.)

#### 📊 Lógica de Clima
**Condiciones soportadas (según código WMO):**
- ☀️ **Soleado** (0): "Es un gran día para salir con tu mascota"
- ⛅ **Parcialmente Nublado** (1-2): "Podría ser divertido salir, pero lleva un abrigo"
- ☁️ **Nublado** (3, 45, 48): "Tal vez no perfecto, pero pueden disfrutar juntos en casa"
- 🌧️ **Lluvioso** (51-67, 80-82): "Mejor descansa y juega con tu mascota en interiores"
- ❄️ **Nevando** (71-77, 85-86): "Hace frío afuera, mejor abrígate y disfruta en casa con tu mascota"
- ⛈️ **Tormenta** (95-99): "Quédate en casa, no es seguro salir con tu mascota"
- 🌡️ **Desconocido**: "Consulta el clima antes de salir con tu mascota"

**Categorías de temperatura:**
- ❄️ Frío: < 10°C
- 🍃 Fresco: 10-17°C
- 🌤️ Templado: 18-24°C
- ☀️ Caluroso: ≥ 25°C

#### ⚡ Optimizaciones
- **Cache temporal:** 5 minutos para evitar requests innecesarios
- **Timeouts configurados:** 15 segundos para conexión/lectura/escritura
- **Retry automático:** En caso de fallo de conexión
- **Coroutines:** Ejecución asíncrona sin bloquear UI
- **StateFlow:** Actualizaciones reactivas del estado

#### 🛡️ Manejo de Errores
**Errores manejados:**
- ❌ Sin conexión a internet (`UnknownHostException`)
- ⏱️ Timeout de conexión (`SocketTimeoutException`)
- 🌐 Errores HTTP (400, 404, 429, 500, 502, 503)
- 📡 Errores de red general (`IOException`)
- 🔧 Errores inesperados (parsing JSON, etc.)

**Mensajes de error descriptivos:**
- Cada tipo de error tiene un mensaje específico en español
- Opción de "Reintentar" disponible en todos los errores
- Loading indicators durante operaciones asíncronas

### Archivos Creados

#### 📦 Data Layer
1. **`WeatherModels.kt`** - Modelos de datos
   - `WeatherCondition` (enum con emojis y mensajes)
   - `CurrentWeather` (temperatura, código WMO, viento)
   - `WeatherResponse` (respuesta completa de API)
   - `WeatherUiState` (sealed class para estados UI)

2. **`WeatherApiService.kt`** - Servicio Retrofit
   - `WeatherApiService` (interface con endpoint)
   - `RetrofitClient` (singleton con configuración OkHttp)

3. **`WeatherRepository.kt`** - Repositorio
   - Cache temporal (5 minutos)
   - Manejo completo de errores
   - Métodos: `getCurrentWeather()`, `getWeatherWithMessage()`, `invalidateCache()`

#### 🎨 Presentation Layer
4. **`WeatherViewModel.kt`** - ViewModel
   - `StateFlow<WeatherUiState>` para estado reactivo
   - Métodos: `loadWeather()`, `refreshWeather()`, `retry()`
   - Helpers: `formatTemperature()`, `getTemperatureCategory()`, `getMessageForCondition()`
   - Prevención de múltiples requests simultáneos

5. **`WeatherView.kt`** - Vista Compose
   - Componentes: `WeatherMainCard`, `WeatherMessageCard`, `WeatherDetailsCard`
   - Estados: `LoadingContent`, `SuccessContent`, `ErrorContent`, `InitialStateContent`
   - Helpers: `getBackgroundColorForCondition()`, `getWindDirectionLabel()`, `formatTime()`

#### 🧪 Testing
6. **`WeatherConditionTest.kt`** - Tests unitarios de modelos
   - Conversión de códigos WMO a condiciones
   - Verificación de mensajes únicos
   - Test de emojis y descripciones
   - Test de `CurrentWeather.getCondition()`

7. **`WeatherViewModelTest.kt`** - Tests del ViewModel
   - Estados UI (Loading, Success, Error)
   - Refresh y retry
   - Formateo de temperatura
   - Categorización térmica
   - Prevención de requests concurrentes

#### 🧭 Navigation
8. **`Route.kt`** (modificado)
   - Agregado `object Weather : Route("clima")`
   - Incluido en `menuItems`

9. **`MenuShellView.kt`** (modificado)
   - Ruta de navegación a `WeatherView()`
   - Ícono `Icons.Default.WbSunny` para clima

### Dependencias Agregadas

```gradle
// Retrofit para consumo de API
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.11.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

// Gson para parseo JSON
implementation("com.google.code.gson:gson:2.10.1")

// Testing
testImplementation("org.mockito:mockito-core:5.3.1")
testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")
testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.1")
testImplementation("androidx.arch.core:core-testing:2.2.0")
```

### Ejecutar Tests

```powershell
# Ejecutar todos los tests
.\gradlew.bat test --no-daemon

# Ejecutar solo tests de clima
.\gradlew.bat test --tests "*Weather*" --no-daemon

# Test específico
.\gradlew.bat test --tests "WeatherConditionTest" --no-daemon
.\gradlew.bat test --tests "WeatherViewModelTest" --no-daemon

# Ver reporte HTML
# Ubicación: app/build/reports/tests/testDebugUnitTest/index.html
```

### Uso de la Funcionalidad

1. **Acceder:** Menú lateral → "Clima"
2. **Visualizar:** Temperatura, condición, mensaje personalizado
3. **Actualizar:** Botón "Actualizar Clima" para refrescar
4. **Reintentar:** En caso de error, usar botón "Reintentar"

### Mejores Prácticas Implementadas

✅ **Arquitectura MVVM** - Separación clara de responsabilidades  
✅ **Repository Pattern** - Abstracción de fuente de datos  
✅ **Coroutines** - Operaciones asíncronas eficientes  
✅ **StateFlow** - Estado reactivo sin LiveData  
✅ **Singleton Pattern** - RetrofitClient único  
✅ **Cache Strategy** - Reducción de consumo de datos  
✅ **Error Handling** - Manejo exhaustivo de excepciones  
✅ **Unit Testing** - Cobertura de lógica crítica  
✅ **Comentarios KDoc** - Documentación completa en código  
✅ **Material Design 3** - UI moderna y consistente  
✅ **Animaciones** - UX fluida y atractiva  
✅ **Accessibility** - Content descriptions y labels  

### Compatibilidad

- ✅ Android 7.0+ (API 24)
- ✅ Kotlin 1.9.0
- ✅ Compose BOM 2023.08.00
- ✅ Sin API key requerida
- ✅ Sin límite de requests (Open-Meteo free tier)
- ✅ Funciona offline (muestra error descriptivo)

---

**Desarrollado para DUOC UC** | *Versión 1.0 - Octubre 2025* | *Clima - Noviembre 2025*

