# 🚂 Integración Railway API - GuauMiau

## 📋 Resumen de Implementación

Se ha implementado exitosamente el consumo de endpoints REST del microservicio Java Spring Boot desplegado en Railway, siguiendo las mejores prácticas para Android Studio Iguana 2023.1.1 Patch 2.

---

## 🎯 Características Implementadas

### ✅ Arquitectura Completa
- **Data Transfer Objects (DTOs)** para intercambio JSON
- **Retrofit Service** con todos los endpoints REST
- **Repository Pattern** con sincronización local/remota
- **ViewModel** con manejo de estados UI
- **Compose UI** con pantalla de prueba interactiva

### ✅ Funcionalidades
- **GET** `/api/pets` - Listar todas las mascotas
- **GET** `/api/pets?userEmail={email}` - Mascotas por usuario
- **POST** `/api/pets` - Crear nueva mascota
- **PUT** `/api/pets/{id}` - Actualizar mascota
- **DELETE** `/api/pets/{id}` - Eliminar mascota
- **Sincronización** automática con Room Database (cache local)
- **Manejo robusto de errores** de red y HTTP
- **Logging detallado** para debugging

---

## 📂 Archivos Creados

### 1️⃣ **DTOs** (`data/remote/dto/`)
```
PetDto.kt          - Modelo de mascota (id, name, type, userEmail)
UserDto.kt         - Modelo de usuario y autenticación
```

### 2️⃣ **Servicios API** (`data/remote/`)
```
RailwayApiService.kt        - Interfaz Retrofit con endpoints
RailwayRetrofitClient.kt    - Cliente singleton con OkHttp
```

### 3️⃣ **Repositorio** (`data/repository/`)
```
RemotePetRepository.kt      - Gestión de sincronización local/remota
```

### 4️⃣ **ViewModel** (`viewmodels/`)
```
RemotePetViewModel.kt       - Lógica de negocio y estados UI
```

### 5️⃣ **UI** (`views/menu/`)
```
RemotePetTestView.kt        - Pantalla de prueba con Compose
```

### 6️⃣ **Navegación** (modificados)
```
Route.kt                    - Agregada ruta RailwayTest
MenuShellView.kt            - Integración en menú lateral
```

---

## 🔧 Configuración Técnica

### Dependencias (ya existentes en build.gradle.kts)
```gradle
// Retrofit 2.9.0 (estable para Iguana 2023.1.1)
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp 4.11.0
implementation("com.squareup.okhttp3:okhttp:4.11.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

// Gson 2.10.1
implementation("com.google.code.gson:gson:2.10.1")
```

### URL del Microservicio
```kotlin
BASE_URL = "https://microservicedm-production.up.railway.app/"
```

### Configuración de Timeouts
```kotlin
CONNECT_TIMEOUT = 30 segundos
READ_TIMEOUT = 30 segundos
WRITE_TIMEOUT = 30 segundos
```

---

## 🚀 Cómo Usar

### 1️⃣ Acceder a la Pantalla de Prueba
1. Ejecutar la app en dispositivo/emulador
2. Abrir menú lateral (☰)
3. Seleccionar **"Railway API"**

### 2️⃣ Funcionalidades Disponibles

#### **Sincronizar Mascotas** 🔄
- Presionar el botón de **Refresh** en la esquina superior derecha
- Descarga mascotas del servidor Railway
- Actualiza la base de datos local (Room)

#### **Crear Mascota** ➕
- Presionar el botón flotante **"Crear Mascota"**
- Ingresar nombre (mínimo 2 caracteres)
- Seleccionar tipo (PERRO, GATO, AVE, OTRO)
- Confirmar creación
- La mascota se crea en Railway y se guarda localmente

#### **Eliminar Mascota** 🗑️
- Presionar el icono de **basura** en la card de la mascota
- Confirmar eliminación
- Se elimina del servidor Railway y de Room

### 3️⃣ Ver Logs de Diagnóstico

#### En Android Studio Logcat:
```
Filtro: RailwayRetrofitClient
- Inicialización del cliente
- Configuración de timeouts
- Base URL

Filtro: RemotePetRepository
- Sincronización de mascotas
- Creación exitosa/fallida
- Eliminación de mascotas
- Errores HTTP detallados

Filtro: RemotePetViewModel
- Cambios de estado UI
- Validación de formularios
- Mensajes de éxito/error
```

---

## 🎨 UI de Prueba - Características

### Info Card
- **URL del API**: https://microservicedm-production.up.railway.app
- **Endpoints disponibles**: GET, POST, PUT, DELETE /api/pets
- **Estado visual** de conexión

### Lista de Mascotas
- **ID de servidor**: Visible para debugging
- **Nombre y tipo**: Claramente identificados
- **Botón eliminar**: Con confirmación de seguridad

### Diálogos
- **Crear mascota**: Validación en tiempo real
- **Eliminar mascota**: Confirmación con advertencia
- **Loading states**: Indicadores visuales durante operaciones

### Snackbars
- **Mensajes de éxito**: Verde, 2 segundos
- **Mensajes de error**: Rojo, 5 segundos
- **Información detallada**: Incluye razón del error

---

## 🔍 Manejo de Errores

### Errores de Red
```kotlin
IOException -> "Error de red: ${message}"
```
Causas:
- Sin conexión a internet
- Timeout (>30s)
- Railway API caído

### Errores HTTP
```kotlin
400 -> "Petición inválida"
401 -> "No autorizado"
404 -> "Recurso no encontrado"
500 -> "Error interno del servidor"
```

### Logs Detallados
Todos los errores se registran con:
- Código HTTP
- Mensaje de error del servidor
- Stack trace completo
- Timestamp

---

## 📊 Flujo de Datos

### Lectura (GET)
```
1. Usuario abre pantalla
2. ViewModel carga mascotas desde Room (cache)
3. Usuario presiona Sync
4. Repository hace GET a Railway API
5. Datos se guardan en Room
6. Flow de Room actualiza UI automáticamente
```

### Escritura (POST)
```
1. Usuario completa formulario
2. ViewModel valida datos
3. Repository hace POST a Railway API
4. Railway devuelve mascota con ID
5. Mascota se guarda en Room con ID del servidor
6. Flow actualiza UI con nueva mascota
```

### Eliminación (DELETE)
```
1. Usuario confirma eliminación
2. Repository hace DELETE a Railway API
3. Si éxito (204), elimina de Room
4. Flow actualiza UI (mascota desaparece)
```

---

## 🧪 Pruebas Recomendadas

### 1️⃣ Conexión
- ✅ Con WiFi activo
- ✅ Con datos móviles
- ✅ Sin conexión (ver error)

### 2️⃣ CRUD Completo
- ✅ Crear mascota con nombre válido
- ✅ Crear con nombre corto (error de validación)
- ✅ Listar mascotas después de crear
- ✅ Eliminar mascota creada
- ✅ Verificar que desaparece de la lista

### 3️⃣ Sincronización
- ✅ Crear mascota en otro dispositivo
- ✅ Presionar Sync en este dispositivo
- ✅ Verificar que aparece

### 4️⃣ Errores
- ✅ Apagar WiFi y crear mascota (error de red)
- ✅ Enviar request inválido (error 400)
- ✅ Verificar logs en Logcat

---

## 🎓 Conceptos Aplicados

### ✅ Compatibilidad con Iguana 2023.1.1 Patch 2
- Retrofit 2.9.0 (estable, no experimental)
- Gson 2.10.1 (sin beta/alpha)
- OkHttp 4.11.0 (probado)
- MinSDK 24 (Android 7.0)

### ✅ Patrón Repository
- Abstracción de fuente de datos
- Sincronización local/remota
- Cache-first strategy

### ✅ MVVM con StateFlow
- Separación de responsabilidades
- UI reactiva con Compose
- Estados inmutables

### ✅ Coroutines
- Operaciones asíncronas en IO dispatcher
- ViewModelScope para lifecycle-aware
- withContext para cambio de dispatcher

### ✅ Error Handling
- Result<T> para encapsular éxito/fallo
- Logging estructurado
- Mensajes amigables al usuario

---

## 📝 Notas Importantes

### ⚠️ URL de Railway
Verifica que el microservicio esté activo:
```bash
curl https://microservicedm-production.up.railway.app/api/pets
```

### ⚠️ Logs en Producción
El logging interceptor está en nivel BODY (muestra JSON completo).
Para producción, cambiar a:
```kotlin
level = HttpLoggingInterceptor.Level.NONE
```

### ⚠️ SSL/TLS
El código incluye configuración SSL compatible con Android 7.0+.
Si hay errores de certificado, verificar `WeatherApiService.kt` para referencia.

### ⚠️ Room + Retrofit
Este patrón permite:
- **Modo offline**: Datos en Room disponibles sin red
- **Sincronización**: Actualización cuando hay conexión
- **Performance**: Cache local reduce latencia

---

## 🔄 Próximos Pasos

### Opcional: Integrar con Pantallas Existentes
Puedes usar `RemotePetRepository` en:
- `ProfileView.kt` - Para mostrar mascotas del servidor
- `RegisterViewModel.kt` - Para guardar mascotas en Railway

### Opcional: Autenticación
Implementar endpoints de usuario:
```kotlin
POST /api/users/register
POST /api/users/login
GET /api/users/{email}
```

### Opcional: Tests Unitarios
Crear tests para:
- `RemotePetRepository` (con MockWebServer)
- `RemotePetViewModel` (con fake repository)

---

## 🎉 Estado Actual

### ✅ Compilación
```
BUILD SUCCESSFUL in 23s
35 actionable tasks: 8 executed, 27 up-to-date
```

### ✅ Archivos Creados
- 6 archivos nuevos (DTOs, Services, Repository, ViewModel, UI)
- 2 archivos modificados (Route, MenuShellView)

### ✅ Funcionalidades
- Sincronización ✓
- Creación ✓
- Eliminación ✓
- Error handling ✓
- Logging ✓
- UI completa ✓

---

## 📞 Soporte

Para debugging, revisar logs con filtros:
- `RailwayRetrofitClient`
- `RemotePetRepository`
- `RemotePetViewModel`

Todos los errores incluyen:
- TAG específico
- Mensaje descriptivo
- Stack trace (si aplica)
- Timestamp

---

**Fecha de implementación**: 21 de Noviembre de 2025  
**Compatible con**: Android Studio Iguana 2023.1.1 Patch 2  
**MinSDK**: 24 (Android 7.0)  
**TargetSDK**: 34  
**Kotlin**: 1.9.0  
**Retrofit**: 2.9.0  
**Gson**: 2.10.1  
**OkHttp**: 4.11.0  

---

**🐾 GuauMiau - Juguetes para Mascotas**  
*Ahora con sincronización en la nube via Railway* 🚂☁️
