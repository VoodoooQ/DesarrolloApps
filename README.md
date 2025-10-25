# 🐾 GuauMiau - Aplicación Android MVVM

## 📱 Descripción

**GuauMiau** es una aplicación Android completa para la tienda de mascotas **GUAU&MIAU**, desarrollada con arquitectura MVVM (Model-View-ViewModel) y las mejores prácticas de desarrollo moderno. La aplicación incluye un sistema completo de autenticación, gestión de usuarios y administración de tareas con persistencia en base de datos local usando Room.

---

## ✨ Características Principales

### 🔐 Sistema de Autenticación Completo
- **Login** con validación de credenciales contra Room Database
- **Registro de usuarios** con persistencia en base de datos
- Validación de correos exclusivamente del dominio **@duoc.cl**
- Contraseñas seguras con validaciones estrictas en tiempo real
- Gestión de sesiones con paso de `userId` entre pantallas
- Opción "Olvidé mi contraseña"

### � Menú Lateral (Navigation Drawer)
- **ModalNavigationDrawer** con navegación fluida
- **5 opciones de menú** con ejemplos educativos:
  - 2.1.3 Componentes - Ejemplos de componentes Material Design 3
  - 2.2.4 Navegación - Sistema de navegación implementado
  - 2.3.3 Formularios - Formularios completos con validaciones
  - 2.4.2 Persistencia y Animaciones - Room DB y animaciones Compose
  - 2.4.4 Función Nativa (Cámara) - Integración con cámara
- **TopAppBar** con ícono de menú hamburguesa
- Navegación entre vistas con **Navigation Compose**

### �📝 Gestión de Tareas (CRUD Completo)
- **Crear** tareas con título, descripción y fecha
- **Leer** lista de tareas filtradas por usuario
- **Actualizar** tareas existentes
- **Eliminar** tareas individualmente o todas
- Marcar tareas como completadas con checkbox
- Diseño Material Design con RecyclerView
- Estados vacíos e indicadores de carga
- Validaciones de formularios

### 🐶 Registro de Mascotas
- Añadir múltiples mascotas durante el registro de usuario
- Tipos soportados: PERRO, GATO, AVE, Otro
- Validación de nombres y tipos obligatorios
- Interfaz dinámica para agregar/eliminar mascotas

---

## 🎯 Funcionalidades Clave

✅ **Autenticación con Room Database**  
✅ **Menú lateral (ModalNavigationDrawer)**  
✅ **Navigation Compose con 5 vistas**  
✅ **CRUD completo de tareas con ViewBinding**  
✅ **Navegación entre pantallas con Intent**  
✅ **Validaciones en tiempo real**  
✅ **Material Design 3**  
✅ **Arquitectura MVVM + Repository Pattern**  
✅ **Coroutines para operaciones asíncronas**  
✅ **LiveData para observación reactiva**  
✅ **Jetpack Compose para Login/Registro/Menú**  
✅ **XML Layouts para pantallas CRUD**  
✅ **Mensajes de confirmación con AlertDialog**  
✅ **Feedback con Snackbar**  
✅ **Animaciones con Compose**  

---

## 🛠️ Especificaciones Técnicas

### Entorno de Desarrollo
| Herramienta | Versión |
|------------|---------|
| **Android Studio** | Hedgehog 2023.1.1 Patch 2 |
| **Kotlin** | 1.9.0 |
| **JVM Target** | 1.8 |
| **Gradle Plugin** | 8.2.2 |
| **Gradle** | 8.2 |
| **Compile SDK** | 34 (Android 14) |
| **Min SDK** | 24 (Android Nougat) |
| **Target SDK** | 34 |

### Dependencias Principales
```kotlin
// Jetpack Compose
implementation(platform("androidx.compose:compose-bom:2023.08.00"))
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Room Database (con KSP)
implementation("androidx.room:room-runtime:2.5.2")
implementation("androidx.room:room-ktx:2.5.2")
ksp("androidx.room:room-compiler:2.5.2")

// Navigation
implementation("androidx.navigation:navigation-fragment-ktx:2.7.1")
implementation("androidx.navigation:navigation-ui-ktx:2.7.1")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

// ViewModel & LiveData
implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
```

---

## 📂 Estructura del Proyecto

La aplicación sigue la arquitectura **MVVM** con **Clean Architecture**:

```
com.example.guaumiau/
├── data/                          # 📦 Capa de Datos
│   ├── local/                     # Base de datos local Room
│   │   ├── AppDatabase.kt         # Database singleton (versión 2)
│   │   ├── TaskDao.kt             # DAO de tareas
│   │   └── UserDao.kt             # DAO de usuarios
│   ├── model/                     # Entidades y modelos
│   │   ├── Task.kt                # Entidad de tarea
│   │   └── UserEntity.kt          # Entidad de usuario
│   ├── repository/                # Repositorios
│   │   ├── TaskRepository.kt      # Repositorio de tareas
│   │   └── UserRepository.kt      # Repositorio de usuarios
│   ├── User.kt                    # Modelo de usuario (UI)
│   ├── PetType.kt                 # Enum de tipos de mascota
│   └── Validator.kt               # Validaciones centralizadas
│
├── navigation/                    # 🧭 Navegación (placeholder)
│
├── ui/                            # 🎨 Capa de Presentación
│   ├── main/                      # Pantallas principales
│   │   ├── TaskListActivity.kt    # Lista de tareas
│   │   └── AddEditTaskActivity.kt # Crear/Editar tarea
│   └── theme/                     # Temas Material Design
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
│
├── viewmodels/                    # 🧠 ViewModels
│   ├── LoginViewModel.kt          # ViewModel de login
│   ├── RegisterViewModel.kt       # ViewModel de registro
│   ├── MainViewModel.kt           # ViewModel de lista de tareas
│   └── AddEditViewModel.kt        # ViewModel de crear/editar
│
├── views/                         # 👁️ Vistas Compose
│   ├── LoginScreen.kt             # Pantalla de login
│   └── RegisterScreen.kt          # Pantalla de registro
│
└── MainActivity.kt                # Actividad principal (Auth)
```

### Layouts XML (res/layout/)
```
├── activity_task_list.xml         # Lista de tareas con RecyclerView
├── activity_add_edit_task.xml     # Formulario crear/editar
└── item_task.xml                  # Card de tarea individual
```

---

## 🏗️ Arquitectura MVVM

### Diagrama de Flujo

```
┌─────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                            │
│  ┌──────────────┐         ┌──────────────┐                      │
│  │   Activity   │ ◄────── │  ViewModel   │                      │
│  │  (Compose/   │         │  (LiveData/  │                      │
│  │  ViewBinding)│         │  StateFlow)  │                      │
│  └──────┬───────┘         └──────┬───────┘                      │
└─────────┼────────────────────────┼──────────────────────────────┘
          │                        │
          │ Observa               │ Llama
          │                        │
┌─────────┼────────────────────────▼──────────────────────────────┐
│         │              DOMAIN LAYER                              │
│         │         ┌──────────────────┐                           │
│         │         │   Repository     │                           │
│         │         │   (Interface)    │                           │
│         │         └────────┬─────────┘                           │
└─────────┼──────────────────┼──────────────────────────────────┘
          │                  │
          │                  │ Implementa
          │                  │
┌─────────▼──────────────────▼──────────────────────────────────┐
│                       DATA LAYER                               │
│  ┌──────────────────────────────────────────┐                  │
│  │        Repository Implementation         │                  │
│  └────────┬──────────────────────┬──────────┘                  │
│           │                      │                              │
│  ┌────────▼────────┐    ┌────────▼────────┐                   │
│  │  Room Database  │    │   Remote API    │                   │
│  │  (Local)        │    │   (Futuro)      │                   │
│  │  ┌───────────┐  │    │                 │                   │
│  │  │    DAO    │  │    │                 │                   │
│  │  └─────┬─────┘  │    │                 │                   │
│  │  ┌─────▼─────┐  │    │                 │                   │
│  │  │  Entity   │  │    │                 │                   │
│  │  └───────────┘  │    │                 │                   │
│  └─────────────────┘    └─────────────────┘                   │
└────────────────────────────────────────────────────────────────┘
```

### Componentes Clave

#### 1️⃣ **Data Layer (Capa de Datos)**
- **Room Database**: Base de datos local SQLite
  - `AppDatabase`: Singleton con versión 2
  - `UserDao`: CRUD de usuarios
  - `TaskDao`: CRUD de tareas
- **Entities**: `UserEntity`, `Task`
- **Repositories**: Abstracción de fuentes de datos

#### 2️⃣ **Domain Layer (Lógica de Negocio)**
- **Models**: `User`, `PetType`, `ValidationResult`
- **Validator**: Validaciones centralizadas
- **Repositories (Interfaces)**: Contratos de datos

#### 3️⃣ **Presentation Layer (UI)**
- **ViewModels**: Estado y lógica de presentación
- **Views**: Compose Screens + XML Layouts
- **Activities**: Contenedores de UI

---

## 🗄️ Base de Datos Room

### Versión Actual: **2**

### Tabla: `users`
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fullName TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    phone TEXT
);
```

**Operaciones UserDao:**
- `insertUser()`: Registrar nuevo usuario
- `authenticate()`: Login con email/password
- `isEmailRegistered()`: Validar duplicados
- `getUserByEmail()`: Obtener usuario

### Tabla: `tasks`
```sql
CREATE TABLE tasks (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    date TEXT NOT NULL,
    userId TEXT NOT NULL,
    isCompleted INTEGER NOT NULL DEFAULT 0
);
```

**Operaciones TaskDao:**
- `getTasksByUser()`: Tareas filtradas por usuario (LiveData)
- `insertTask()`: Crear nueva tarea
- `updateTask()`: Actualizar tarea
- `deleteTask()`: Eliminar tarea
- `updateTaskStatus()`: Marcar completada/pendiente
- `deleteAllTasksByUser()`: Eliminar todas las tareas

---

## 🎨 Validaciones Implementadas

### ✉️ Email
- ✅ Formato válido de email
- ✅ Debe terminar en **@duoc.cl**
- ✅ No puede estar vacío
- ✅ Verificación de duplicados al registrar

### 🔒 Contraseña
- ✅ Mínimo **8 caracteres**
- ✅ Al menos **1 letra mayúscula**
- ✅ Al menos **1 letra minúscula**
- ✅ Al menos **1 número**
- ✅ Al menos **1 carácter especial** (!@#$%^&*)
- ✅ Confirmación de contraseña debe coincidir

### 👤 Otros Campos
- **Nombre completo**: 2-100 caracteres
- **Teléfono**: Formato chileno +56 9 XXXX XXXX (opcional)
- **Título de tarea**: 3-100 caracteres
- **Descripción de tarea**: 5-500 caracteres

---

## 🚀 Guía de Inicio Rápido

### 1️⃣ Requisitos Previos
- Android Studio Hedgehog 2023.1.1 Patch 2 o superior
- JDK 8 o superior
- Emulador o dispositivo con Android 7.0 (API 24) o superior

### 2️⃣ Clonar y Abrir Proyecto
```bash
# Clonar repositorio
git clone <repository-url>

# Abrir en Android Studio
File > Open > Seleccionar carpeta GuauMiau
```

### 3️⃣ Sincronizar Dependencias
```
File > Sync Project with Gradle Files
```

### 4️⃣ Compilar Proyecto
```powershell
# Desde terminal (PowerShell)
cd "ruta\a\GuauMiau"
.\gradlew.bat clean assembleDebug --no-daemon

# Desde Android Studio
Build > Clean Project
Build > Rebuild Project
```

### 5️⃣ Ejecutar App
```
Run > Run 'app' (Shift + F10)
```

---

## 📖 Guía de Uso

### 🔐 Registro de Nuevo Usuario

1. **Abrir la app** → Pantalla de Login
2. **Click en "REGISTRO"** (botón superior derecho)
3. **Llenar el formulario:**
   - Nombre completo
   - Email (debe terminar en @duoc.cl)
   - Contraseña (con validaciones estrictas)
   - Confirmar contraseña
   - Teléfono (opcional)
   - Mascotas (opcional, usar "AÑADIR NUEVO REGISTRO")
4. **Click en "REGISTRARSE"**
5. Si el registro es exitoso:
   - ✅ Usuario guardado en Room Database
   - ✅ Mensaje de confirmación
   - ✅ Redirección a Login

### 🔓 Iniciar Sesión

1. **Ingresar email y contraseña** del usuario registrado
2. **Click en "INGRESAR"**
3. Sistema autentica con la base de datos:
   - ✅ Busca usuario con ese email
   - ✅ Verifica contraseña
4. Login exitoso → Navegación a lista de tareas

### ✏️ Gestionar Tareas

#### Crear Tarea
1. En TaskListActivity, click en **FAB (+)**
2. Llenar formulario (título y descripción)
3. Click en **Guardar** (ícono superior)
4. Tarea aparece en la lista

#### Editar Tarea
1. Click en una tarea de la lista
2. Modificar campos
3. Click en **Guardar**

#### Marcar como Completada
1. Click en el **checkbox** de la tarea
2. El título se tacha automáticamente

#### Eliminar Tarea
1. Click en el **ícono de basura** (🗑️) en la tarea
2. Confirmar en el diálogo

#### Eliminar Todas las Tareas
1. Menu (⋮) → "Eliminar todas las tareas"
2. Confirmar en el diálogo

#### Cerrar Sesión
1. Menu (⋮) → "Cerrar sesión"
2. Vuelve a la pantalla de Login

---

## 🧪 Ejemplo de Prueba

### Flujo Completo de Prueba

```kotlin
// 1. REGISTRAR USUARIO
Email:              juan.perez@duoc.cl
Password:           MiPass123!
Nombre Completo:    Juan Pérez González
Teléfono:           +56 9 1234 5678

// 2. INICIAR SESIÓN
Email:              juan.perez@duoc.cl
Password:           MiPass123!

// 3. CREAR TAREA
Título:             Comprar alimento para perros
Descripción:        Comprar 5kg de alimento premium para Max

// 4. CREAR OTRA TAREA
Título:             Vacuna antirrábica
Descripción:        Llevar a Luna al veterinario el viernes

// 5. MARCAR COMPLETADA
- Click en checkbox de "Comprar alimento para perros"

// 6. ELIMINAR TAREA
- Click en 🗑️ de "Vacuna antirrábica"
```

---

## 🔒 Seguridad

⚠️ **IMPORTANTE - Solo para desarrollo:**
- Las contraseñas se almacenan en **texto plano** en la base de datos local
- En un entorno de **producción**, se debe:
  - Implementar **hashing** de contraseñas (bcrypt, Argon2, etc.)
  - Usar **tokens JWT** para sesiones
  - Implementar **SSL/TLS** para comunicaciones
  - Añadir **autenticación de dos factores** (2FA)
  - Usar **SharedPreferences encriptadas** para datos sensibles

---

## 📊 Estado del Proyecto

### ✅ Completado
- [x] Estructura MVVM con 5 carpetas
- [x] Login con Compose y validaciones
- [x] Registro con Compose y validaciones dinámicas
- [x] Room Database con 2 tablas (users, tasks)
- [x] UserDao y TaskDao completos
- [x] UserRepository y TaskRepository
- [x] Autenticación real con base de datos
- [x] CRUD completo de tareas
- [x] ViewModels con LiveData
- [x] Navegación entre pantallas
- [x] Material Design 3
- [x] ViewBinding en pantallas CRUD
- [x] RecyclerView con DiffUtil
- [x] AlertDialogs de confirmación
- [x] Snackbars para feedback
- [x] Estados vacíos e indicadores de carga
- [x] Migración de kapt a KSP
- [x] Compilación exitosa sin errores

### 🎯 Funcionalidades Opcionales Futuras
- [ ] Implementar hashing de contraseñas (bcrypt)
- [ ] Recuperación de contraseña por email
- [ ] Edición de perfil de usuario
- [ ] Foto de perfil con Coil
- [ ] Autenticación biométrica (huella/rostro)
- [ ] Sincronización con backend (Firebase/API REST)
- [ ] Notificaciones push
- [ ] Modo oscuro (Dark Theme)
- [ ] Categorías y prioridades de tareas
- [ ] Fechas de vencimiento y recordatorios
- [ ] Búsqueda y filtros avanzados
- [ ] Swipe to delete en RecyclerView
- [ ] Exportar tareas a PDF/Excel
- [ ] Tests unitarios (JUnit, Mockito)
- [ ] Tests de UI (Espresso, Compose Testing)

---

## 🐛 Solución de Problemas

### Error: "kapt incompatible with Java 17+"
✅ **Solucionado**: Migrado a **KSP** (Kotlin Symbol Processing)

### Error: "Build failed with compilation errors"
```powershell
# Limpiar y reconstruir
.\gradlew.bat clean build --no-daemon
```

### Error: "No se puede iniciar sesión"
- Verificar que el usuario esté registrado
- Email debe ser **@duoc.cl**
- Contraseña debe cumplir requisitos de seguridad

### App se cierra al abrir
- Verificar logs en Logcat
- Revisar versión de Android (mínimo API 24)
- Reinstalar la aplicación

---

## 📝 Notas Técnicas

### KSP vs KAPT
El proyecto usa **KSP** (Kotlin Symbol Processing) en lugar de **KAPT** debido a:
- ✅ Mejor rendimiento (2x más rápido)
- ✅ Compatible con Java 17+
- ✅ Menor consumo de memoria
- ✅ Recomendado por Google para Room

### ViewBinding vs Compose
- **Compose**: Usado en Login/Register (UI declarativa moderna)
- **ViewBinding**: Usado en CRUD (integración con XML layouts)

---

## 👨‍💻 Desarrollo

### Compilación desde Terminal
```powershell
# Windows PowerShell
cd "c:\Users\MAAXXDC\Desktop\Nueva carpeta\GuauMiau"

# Limpiar proyecto
.\gradlew.bat clean

# Compilar Debug
.\gradlew.bat assembleDebug --no-daemon

# Compilar Release
.\gradlew.bat assembleRelease --no-daemon

# Ejecutar tests
.\gradlew.bat test

# Generar APK firmado
.\gradlew.bat bundleRelease
```

### Estructura de Commits Recomendada
```
feat: Agregar autenticación con Room Database
fix: Corregir validación de email
refactor: Migrar de kapt a KSP
docs: Actualizar README con guía de uso
style: Aplicar formato Kotlin
```

---

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos para el **Instituto Profesional DUOC UC**.

---

## 🙏 Agradecimientos

- **Material Design 3** por los componentes de UI
- **JetBrains** por Kotlin y KSP
- **Google** por Android Jetpack (Compose, Room, Navigation)
- **DUOC UC** por el contexto educativo

---

## 📞 Contacto

Para preguntas o sugerencias sobre este proyecto:
- Repository: [VoodoooQ/DesarrolloApps](https://github.com/VoodoooQ/DesarrolloApps)
- Email: contacto@duoc.cl

---

**Desarrollado con ❤️ para GuauMiau** 🐶🐱

*Última actualización: Octubre 24, 2025*
