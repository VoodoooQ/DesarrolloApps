# Sistema de Autenticación - GuauMiau

## 🎯 Problema Resuelto

El sistema ahora permite **registrar nuevos usuarios** y **autenticarlos** correctamente usando **Room Database** para persistir los datos.

## 📋 Cambios Implementados

### 1. **Nueva Entidad UserEntity** 
- Archivo: `data/model/UserEntity.kt`
- Tabla Room: `users`
- Campos: id, fullName, email, password, phone

### 2. **UserDao - Acceso a Datos**
- Archivo: `data/local/UserDao.kt`
- Métodos:
  - `insertUser()`: Registra un nuevo usuario
  - `authenticate()`: Verifica email y contraseña
  - `isEmailRegistered()`: Valida si un email ya existe
  - `getUserByEmail()`: Obtiene usuario por email

### 3. **UserRepository - Capa de Negocio**
- Archivo: `data/repository/UserRepository.kt`
- Maneja toda la lógica de usuarios
- Usa Coroutines para operaciones asíncronas con `Dispatchers.IO`

### 4. **Base de Datos Actualizada**
- `AppDatabase.kt` actualizado a **versión 2**
- Incluye tanto `Task` como `UserEntity`
- Proporciona `userDao()` y `taskDao()`

### 5. **ViewModels Actualizados**

#### **RegisterViewModel**
- Ahora recibe `UserRepository` como dependencia
- Guarda usuarios reales en la base de datos
- Valida si el email ya está registrado
- Muestra mensajes de error específicos

#### **LoginViewModel**
- Ahora recibe `UserRepository` como dependencia
- Autentica con la base de datos
- Devuelve el `userId` (email) del usuario autenticado
- Gestiona errores de credenciales incorrectas

### 6. **MainActivity Actualizada**
- Inicializa `UserRepository`
- Crea instancias de ViewModels con el repositorio
- Pasa el `userId` real a `TaskListActivity` después del login

### 7. **Pantallas Actualizadas**

#### **RegisterScreen**
- Recibe `viewModel` como parámetro (ya no usa `viewModel()` por defecto)
- Muestra `Snackbar` con mensajes de éxito/error
- Mensaje de confirmación: "¡Registro exitoso! Ahora puedes iniciar sesión"

#### **LoginScreen**
- Recibe `viewModel` como parámetro
- Callback `onLoginSuccess` ahora recibe `userId: String`
- Pasa el email del usuario autenticado al siguiente Activity

## 🔄 Flujo de Uso

### Registro de Nuevo Usuario

1. Usuario abre la app → Pantalla de Login
2. Click en "REGISTRO" (botón superior derecho)
3. Llena el formulario con validaciones:
   - Email debe terminar en `@duoc.cl`
   - Contraseña debe tener mayúscula, minúscula, número y carácter especial
   - Mínimo 8 caracteres
4. Click en "REGISTRARSE"
5. Sistema verifica:
   - ✅ Validaciones de campos
   - ✅ Email no registrado previamente
6. Usuario se guarda en Room Database
7. Mensaje de éxito y redirección a Login

### Login de Usuario Existente

1. Usuario ingresa email y contraseña
2. Click en "INGRESAR"
3. Sistema busca en la base de datos:
   - ✅ Usuario existe con ese email
   - ✅ Contraseña coincide
4. Autenticación exitosa
5. Navegación a `TaskListActivity` con el `userId` del usuario

## 🗄️ Estructura de Base de Datos

### Tabla: `users`
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    fullName TEXT NOT NULL,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    phone TEXT
);
```

### Tabla: `tasks` (existente)
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

## 🔐 Seguridad

⚠️ **IMPORTANTE - Solo para desarrollo:**
- Las contraseñas se almacenan en **texto plano**
- En producción, se debe usar **hashing** (bcrypt, Argon2, etc.)

## ✅ Validaciones Implementadas

### Email
- Formato válido
- Debe terminar en `@duoc.cl`
- No puede estar vacío
- Verificación de duplicados al registrar

### Contraseña
- Mínimo 8 caracteres
- Al menos 1 mayúscula
- Al menos 1 minúscula
- Al menos 1 número
- Al menos 1 carácter especial
- Confirmación de contraseña debe coincidir

### Otros Campos
- Nombre completo: 2-100 caracteres
- Teléfono: formato chileno +56 9 XXXX XXXX (opcional)

## 📱 Ejemplo de Uso

```kotlin
// Registrar un usuario
val newUser = UserEntity(
    fullName = "Juan Pérez",
    email = "juan.perez@duoc.cl",
    password = "MiPass123!",
    phone = "+56 9 1234 5678"
)
userRepository.registerUser(newUser)

// Autenticar usuario
val user = userRepository.authenticateUser(
    email = "juan.perez@duoc.cl",
    password = "MiPass123!"
)
if (user != null) {
    // Login exitoso
    navigateToTasks(user.email)
}
```

## 🧪 Testing

Para probar el sistema:

1. **Compilar**: `.\gradlew.bat assembleDebug`
2. **Ejecutar** en emulador o dispositivo
3. **Registrar** un nuevo usuario con email @duoc.cl
4. **Cerrar sesión** (botón en TaskListActivity)
5. **Iniciar sesión** con las credenciales registradas
6. **Verificar** que las tareas se filtran por usuario

## 🎨 Mejoras Futuras

- [ ] Implementar hashing de contraseñas (bcrypt)
- [ ] Recuperación de contraseña por email
- [ ] Edición de perfil de usuario
- [ ] Foto de perfil
- [ ] Autenticación biométrica
- [ ] Tokens JWT para sesiones
- [ ] Integración con Firebase Auth
- [ ] Sesión persistente (SharedPreferences)

## 📂 Archivos Modificados/Creados

### Creados:
- `data/model/UserEntity.kt`
- `data/local/UserDao.kt`
- `data/repository/UserRepository.kt`
- `SISTEMA_AUTENTICACION.md` (este archivo)

### Modificados:
- `data/local/AppDatabase.kt`
- `viewmodels/LoginViewModel.kt`
- `viewmodels/RegisterViewModel.kt`
- `views/LoginScreen.kt`
- `views/RegisterScreen.kt`
- `MainActivity.kt`

## ✨ Compilación Exitosa

```bash
BUILD SUCCESSFUL in 18s
35 actionable tasks: 9 executed, 26 up-to-date
```

---

**Desarrollado con ❤️ para GuauMiau** 🐶🐱
