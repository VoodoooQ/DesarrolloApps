# Pantallas de Login y Registro - GUAU&MIAU

## 📱 Descripción General

Se han implementado las pantallas de **Login** y **Registro** para la aplicación móvil GUAU&MIAU utilizando **Kotlin** y **Jetpack Compose**, siguiendo los requisitos especificados en los mockups y documentación del proyecto.

---

## 🎯 Características Implementadas

### 1. Pantalla de Login (`LoginScreen.kt`)

#### Diseño UI:
- ✅ Header con título "GUAU&MIAU"
- ✅ Botón "INICIAR SESIÓN" en la esquina superior derecha
- ✅ Formulario central con título "Inicio de sesión"
- ✅ Campo de texto para **CORREO**
- ✅ Campo de texto tipo contraseña para **CONTRASEÑA**
- ✅ Botón "Mostrar/Ocultar contraseña"
- ✅ Enlace "Olvidé mi contraseña" (requerimiento adicional)
- ✅ Botón principal "INICIAR SESIÓN"

#### Funcionalidades:
- ✅ Sistema de inicio de sesión funcional
- ✅ Validación de campos en tiempo real
- ✅ Mensaje de error claro: "El nombre de usuario (correo) o la contraseña son incorrectos"
- ✅ Indicador de carga durante el proceso de login
- ✅ Navegación a pantalla de registro

**Credenciales de prueba:**
- Email: `admin@duoc.cl`
- Contraseña: `Admin123!`

---

### 2. Pantalla de Registro (`RegisterScreen.kt`)

#### Diseño UI:
- ✅ Header idéntico al de login
- ✅ Título "Registro de usuario"
- ✅ Formulario principal con campos:
  - **NOMBRE COMPLETO**
  - **CORREO**
  - **CONTRASEÑA** (con botón mostrar/ocultar)
  - **CONFIRMAR CONTRASEÑA** (con botón mostrar/ocultar)
  - **TELÉFONO (opcional)**

#### Sección Dinámica - Registro de Mascotas:
- ✅ Título "REGISTRO DE MASCOTA"
- ✅ Botón "AÑADIR NUEVO REGISTRO"
- ✅ Formularios dinámicos de mascotas con:
  - Campo "Nombre de la mascota"
  - Dropdown "Seleccione un tipo de mascota" (PERRO, GATO, AVE, Otro)
  - Botón "ELIMINAR" para cada mascota
- ✅ Botón principal "REGISTRAR"

---

## ✅ Validaciones en Tiempo Real

### Campos de Usuario:

#### **Nombre Completo:**
- ❌ No puede estar vacío: _"El nombre completo es obligatorio"_
- ❌ Solo letras y espacios: _"El nombre solo puede contener letras y espacios"_
- ❌ Máximo 50 caracteres: _"El nombre no puede exceder los 50 caracteres"_

#### **Correo Electrónico:**
- ❌ No puede estar vacío: _"El correo electrónico es obligatorio"_
- ❌ Formato válido: _"Formato de correo inválido"_
- ❌ **Solo dominio @duoc.cl**: _"Solo se permiten correos con dominio @duoc.cl"_

#### **Contraseña:**
- ❌ Mínimo 8 caracteres: _"La contraseña debe tener al menos 8 caracteres"_
- ❌ Al menos 1 mayúscula: _"La contraseña debe contener al menos una mayúscula"_
- ❌ Al menos 1 minúscula: _"La contraseña debe contener al menos una minúscula"_
- ❌ Al menos 1 número: _"La contraseña debe contener al menos un número"_
- ❌ Al menos 1 carácter especial (@#$%&*!?_-): _"La contraseña debe contener al menos un carácter especial"_

#### **Confirmar Contraseña:**
- ❌ No puede estar vacía: _"Debe confirmar la contraseña"_
- ❌ Debe coincidir: _"Las contraseñas no coinciden"_

#### **Teléfono (Opcional):**
- ❌ Solo números: _"El teléfono solo puede contener números"_
- ❌ Mínimo 8 dígitos: _"El teléfono debe tener al menos 8 dígitos"_
- ❌ Máximo 15 dígitos: _"El teléfono no puede exceder los 15 dígitos"_

### Registro de Mascotas:

#### **Nombre de la Mascota:**
- ❌ Campo obligatorio: _"El nombre de la mascota es obligatorio"_
- ❌ Máximo 50 caracteres: _"El nombre no puede exceder los 50 caracteres"_

#### **Tipo de Mascota:**
- ❌ Campo obligatorio: _"Debe seleccionar un tipo de mascota"_
- ✅ Opciones: PERRO, GATO, AVE, Otro

---

## 🗂️ Estructura de Archivos

### Archivos Creados:

```
app/src/main/java/com/example/guaumiau/
├── data/
│   ├── User.kt                    # Modelo de datos de Usuario
│   ├── PetRegistration.kt         # Modelo para registro de mascota
│   ├── PetType.kt                 # Enum de tipos de mascota
│   ├── ValidationResult.kt        # Resultado de validaciones
│   └── Validator.kt               # Lógica de validación centralizada
├── viewmodels/
│   ├── LoginViewModel.kt          # ViewModel para Login
│   └── RegisterViewModel.kt       # ViewModel para Registro
├── views/
│   ├── LoginScreen.kt             # Pantalla de Login (Compose)
│   └── RegisterScreen.kt          # Pantalla de Registro (Compose)
└── MainActivity.kt                # Actividad principal con navegación
```

---

## 🏗️ Arquitectura

### Patrón MVVM (Model-View-ViewModel):

1. **Model (data/):** 
   - Modelos de datos: `User`, `PetRegistration`, `PetType`
   - Lógica de validación: `Validator`

2. **ViewModel (viewmodels/):**
   - `LoginViewModel`: Maneja el estado y lógica de Login
   - `RegisterViewModel`: Maneja el estado y lógica de Registro

3. **View (views/):**
   - `LoginScreen`: UI declarativa con Compose
   - `RegisterScreen`: UI declarativa con Compose

### Estado de UI con StateFlow:
- Los ViewModels exponen el estado a través de `StateFlow`
- Las pantallas reaccionan automáticamente a cambios de estado
- Validaciones en tiempo real al escribir

---

## 🎨 Diseño y UX

### Mejoras de Experiencia de Usuario:

1. **Mensajes de Error Claros:**
   - Cada campo muestra mensajes específicos de error
   - Los errores aparecen inmediatamente al escribir (validación en tiempo real)

2. **Feedback Visual:**
   - Campos con error se resaltan en rojo
   - Indicador de carga durante operaciones asíncronas
   - Botones "Mostrar/Ocultar contraseña" en todos los campos de password

3. **Gestión Dinámica de Mascotas:**
   - Añadir/eliminar formularios de mascotas sin límite
   - Numeración automática de mascotas
   - Botón de eliminar individual por mascota

4. **Navegación Intuitiva:**
   - Botón "INICIAR SESIÓN" en header para navegar entre pantallas
   - Teclado virtual adaptado (email, password, teléfono)
   - Acciones de teclado (Next, Done) para mejor flujo

---

## 🔧 Tecnologías Utilizadas

- **Kotlin** 1.9.0
- **Jetpack Compose** (Material3)
- **ViewModel** (androidx.lifecycle)
- **StateFlow** (kotlinx.coroutines)
- **Material Design 3**

---

## 📝 Notas de Implementación

### Resolución de Conflictos del Mockup:

1. **Pág. 5 (Mockup con 2 campos de CORREO):**
   - ✅ IGNORADO según requisitos
   - ✅ Se implementó campo "CONFIRMAR CONTRASEÑA" según pág. 3

2. **Campo "NOMBRE COMPLETO" en mascota:**
   - ✅ Cambiado a "Nombre de la mascota" según requisitos

3. **Opción "Olvidé mi contraseña":**
   - ✅ AÑADIDA aunque no aparece en mockup (requisito explícito)

### Validación Crítica del Dominio:

```kotlin
// Solo acepta correos @duoc.cl
!email.lowercase().endsWith("@duoc.cl")
```

### Simulación de Backend:

- El login actualmente simula una autenticación básica
- Credenciales hardcodeadas para pruebas: `admin@duoc.cl` / `Admin123!`
- El registro simula un delay de 1.5s y retorna éxito

---

## 🚀 Próximos Pasos

1. Integrar con backend real (API REST)
2. Implementar recuperación de contraseña
3. Validar unicidad de correo contra base de datos
4. Persistir sesión de usuario
5. Añadir imágenes de perfil y mascotas
6. Implementar navegación completa de la app

---

## ✨ Cumplimiento de Requisitos

| Requisito | Estado |
|-----------|--------|
| Pantalla de Login según mockup | ✅ |
| Pantalla de Registro según mockup | ✅ |
| Validaciones en tiempo real | ✅ |
| Mensajes de error personalizados | ✅ |
| Solo dominio @duoc.cl | ✅ |
| Validación de contraseña compleja | ✅ |
| Confirmación de contraseña | ✅ |
| Teléfono opcional con validación | ✅ |
| Registro dinámico de mascotas | ✅ |
| Botón "Olvidé mi contraseña" | ✅ |
| Tipos de mascota: PERRO, GATO, AVE, Otro | ✅ |
| Botón ELIMINAR por mascota | ✅ |

---

**Fecha de implementación:** 24 de Octubre, 2025  
**Versión:** 1.0  
**Desarrollado con:** Kotlin + Jetpack Compose
