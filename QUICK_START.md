# 🚀 Guía Rápida - GuauMiau

## 📱 Inicio Rápido

### 1. Sincronizar Proyecto
```
1. Abrir Android Studio Hedgehog 2023.1.1 Patch 2
2. File > Open > Seleccionar carpeta GuauMiau
3. Esperar a que Gradle sincronice
4. File > Sync Project with Gradle Files
```

### 2. Configurar Emulador
```
- API Level: 24 o superior (Nougat+)
- Dispositivo recomendado: Pixel 3a
- RAM: Mínimo 2GB
```

### 3. Ejecutar App
```
1. Conectar dispositivo físico o iniciar emulador
2. Run > Run 'app' (Shift+F10)
3. Esperar compilación
4. ¡La app se instalará automáticamente!
```

## 🔨 Compilación Manual

### Desde Android Studio
```kotlin
// Menú Build
Build > Clean Project
Build > Rebuild Project
Build > Make Project
```

### Desde Terminal
```bash
# En Windows PowerShell
cd "c:\Users\MAAXXDC\Desktop\Nueva carpeta\GuauMiau"

# Limpiar
.\gradlew clean

# Compilar
.\gradlew build

# Compilar solo debug
.\gradlew assembleDebug

# Instalar en dispositivo conectado
.\gradlew installDebug

# Compilar y ejecutar
.\gradlew installDebug && adb shell am start -n com.example.guaumiau/.MainActivity
```

## 📝 Cómo Agregar Datos de Prueba

### Opción 1: Automático (Recomendado para desarrollo)

1. Abrir `AndroidManifest.xml`
2. Agregar nombre de aplicación:
```xml
<application
    android:name=".GuauMiauApplication"
    ...>
```

3. Abrir `GuauMiauApplication.kt`
4. Descomentar línea:
```kotlin
// initializeSampleData() → initializeSampleData()
```

5. Dentro del método, descomentar:
```kotlin
// petDao.insertPets(samplePets) → petDao.insertPets(samplePets)
```

### Opción 2: Manual desde código

En `MainActivity.kt`, agregar en `onCreate()`:
```kotlin
// Agregar datos de ejemplo
lifecycleScope.launch {
    SampleData.samplePets.forEach { pet ->
        viewModel.addPet(pet)
    }
}
```

### Opción 3: Usando adb y SQLite
```bash
# Conectarse a la shell del dispositivo
adb shell

# Navegar a la base de datos
cd /data/data/com.example.guaumiau/databases

# Abrir SQLite
sqlite3 guaumiau_database

# Insertar mascota
INSERT INTO pets (name, type, breed, age, description) 
VALUES ('Firulais', 'perro', 'Labrador', 3, 'Un perro amigable');

# Ver datos
SELECT * FROM pets;

# Salir
.exit
```

## 🎨 Personalizar la App

### Cambiar Nombre de la App
```xml
<!-- res/values/strings.xml -->
<string name="app_name">MiAppMascotas</string>
```

### Cambiar Colores
```xml
<!-- res/values/colors.xml -->
<color name="purple_500">#FF6200EE</color>
<color name="purple_700">#FF3700B3</color>
```

### Cambiar Ícono
```
1. Click derecho en res
2. New > Image Asset
3. Configurar tu ícono
4. Finish
```

## 🛠️ Modificar Modelos

### Agregar campo a Pet
```kotlin
// 1. domain/model/Pet.kt
data class Pet(
    ...
    val weight: Double? = null  // ← Nuevo campo
)

// 2. data/local/entity/PetEntity.kt
@Entity(tableName = "pets")
data class PetEntity(
    ...
    val weight: Double?  // ← Agregar aquí también
)

// 3. Actualizar versión de DB en AppDatabase.kt
@Database(..., version = 2)  // ← Incrementar versión

// 4. Actualizar DAO si necesitas queries especiales
@Query("SELECT * FROM pets WHERE weight > :minWeight")
fun getPetsHeavierThan(minWeight: Double): Flow<List<PetEntity>>
```

## 📡 Conectar API Real

### 1. Actualizar RetrofitClient
```kotlin
// data/remote/RetrofitClient.kt
private const val BASE_URL = "https://tu-api.com/api/"
```

### 2. Actualizar PetApiService según tu API
```kotlin
@GET("pets")
suspend fun getAllPets(): Response<List<PetDto>>

@POST("pets")
suspend fun createPet(@Body pet: PetDto): Response<PetDto>
```

### 3. Llamar sincronización en MainActivity
```kotlin
lifecycleScope.launch {
    viewModel.syncFromRemote() // Implementar este método
}
```

## 🧪 Testing

### Tests Unitarios (JUnit)
```bash
# Ejecutar tests unitarios
.\gradlew test

# Ver reporte
# Abre: app/build/reports/tests/testDebugUnitTest/index.html
```

### Tests de UI (Espresso)
```bash
# Ejecutar tests instrumentados
.\gradlew connectedAndroidTest
```

## 🐛 Solución de Problemas Comunes

### Error: "Unsupported class file major version"
```
✓ Verificar JDK es versión 17
✓ File > Project Structure > SDK Location > JDK Location
```

### Error: "Failed to resolve: androidx.core:core-ktx:1.10.1"
```
✓ Sync Gradle
✓ File > Invalidate Caches / Restart
✓ Verificar conexión a Internet
```

### Error: "Cannot resolve symbol 'R'"
```
✓ Build > Clean Project
✓ Build > Rebuild Project
✓ File > Sync Project with Gradle Files
```

### App crashea al inicio
```
✓ Ver Logcat (Alt+6)
✓ Filtrar por "AndroidRuntime"
✓ Buscar "FATAL EXCEPTION"
```

### Room error: "Cannot find implementation for AppDatabase"
```
✓ Verificar kapt plugin está aplicado
✓ Sync Gradle
✓ Clean & Rebuild
```

## 📊 Ver Base de Datos

### Opción 1: Device File Explorer
```
1. View > Tool Windows > Device File Explorer
2. Navegar a: data/data/com.example.guaumiau/databases/
3. Descargar guaumiau_database
4. Abrir con DB Browser for SQLite
```

### Opción 2: App Inspection (Android Studio)
```
1. View > Tool Windows > App Inspection
2. Seleccionar Database Inspector
3. Ver tablas y datos en tiempo real
```

## 🔄 Actualizar Dependencias

```kotlin
// Verificar versiones actualizadas compatibles con tu setup
// NO actualizar más allá de lo compatible con API 24

dependencies {
    // Revisar en https://developer.android.com/jetpack/androidx/versions
    implementation("androidx.core:core-ktx:1.10.1") // OK
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1") // OK
    // etc.
}
```

## 📖 Recursos Útiles

### Documentación Oficial
- [Android Developers](https://developer.android.com)
- [Kotlin Docs](https://kotlinlang.org/docs/home.html)
- [Room Persistence](https://developer.android.com/training/data-storage/room)

### Herramientas
- [DB Browser for SQLite](https://sqlitebrowser.org/)
- [Postman](https://www.postman.com/) - Para probar APIs
- [JSON Formatter](https://jsonformatter.org/)

### Comunidad
- [Stack Overflow - Android](https://stackoverflow.com/questions/tagged/android)
- [r/androiddev](https://reddit.com/r/androiddev)

## ✅ Checklist Pre-Compilación

- [ ] Gradle sincronizado correctamente
- [ ] Sin errores en archivos Kotlin
- [ ] Permisos agregados en AndroidManifest
- [ ] Dispositivo/emulador conectado
- [ ] Espacio suficiente en dispositivo
- [ ] Internet disponible (para descargar dependencias)

## 🎯 Próximos Pasos Sugeridos

1. ✅ Ejecutar app y verificar que compila
2. ✅ Agregar datos de ejemplo
3. ✅ Implementar botón "Agregar Mascota" (FAB)
4. ✅ Crear DetailActivity para ver detalles
5. ✅ Implementar Navigation Component
6. ✅ Agregar búsqueda
7. ✅ Implementar filtros
8. ✅ Conectar API real
9. ✅ Agregar tests

---
**¡Listo para comenzar a desarrollar! 🚀**
