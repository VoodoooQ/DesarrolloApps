# Arquitectura MVVM - GuauMiau

## Diagrama de Flujo de Datos

```
┌─────────────────────────────────────────────────────────────────┐
│                         PRESENTATION                             │
│  ┌──────────────┐         ┌──────────────┐                      │
│  │  MainActivity│ ◄────── │  PetViewModel│                      │
│  └──────┬───────┘         └──────┬───────┘                      │
│         │                        │                               │
│         │                        │ StateFlow/LiveData            │
│  ┌──────▼───────┐                │                               │
│  │  PetAdapter  │                │                               │
│  └──────────────┘                │                               │
└──────────────────────────────────┼───────────────────────────────┘
                                   │
                                   │ Observa cambios
                                   │
┌──────────────────────────────────▼───────────────────────────────┐
│                           DOMAIN                                  │
│  ┌──────────────┐         ┌──────────────┐                       │
│  │GetPetsUseCase│         │AddPetUseCase │                       │
│  └──────┬───────┘         └──────┬───────┘                       │
│         │                        │                                │
│         │                        │                                │
│  ┌──────▼────────────────────────▼───────┐                       │
│  │          Pet (Model)                  │                       │
│  └───────────────────────────────────────┘                       │
└──────────────────────────────────┬───────────────────────────────┘
                                   │
                                   │ Llama al repositorio
                                   │
┌──────────────────────────────────▼───────────────────────────────┐
│                             DATA                                  │
│  ┌───────────────────────────────────────┐                       │
│  │        PetRepository                  │                       │
│  └────────┬──────────────────────┬───────┘                       │
│           │                      │                                │
│           │                      │                                │
│  ┌────────▼────────┐    ┌────────▼────────┐                     │
│  │  Local (Room)   │    │  Remote (API)   │                     │
│  │                 │    │                  │                     │
│  │  ┌───────────┐  │    │  ┌────────────┐ │                     │
│  │  │  PetDao   │  │    │  │PetApiService│ │                    │
│  │  └─────┬─────┘  │    │  └──────┬─────┘ │                     │
│  │        │        │    │         │        │                     │
│  │  ┌─────▼─────┐  │    │  ┌──────▼─────┐ │                     │
│  │  │ PetEntity │  │    │  │   PetDto   │ │                     │
│  │  └───────────┘  │    │  └────────────┘ │                     │
│  │        │        │    │         │        │                     │
│  │  ┌─────▼─────┐  │    │  ┌──────▼─────┐ │                     │
│  │  │AppDatabase│  │    │  │  Retrofit  │ │                     │
│  │  └───────────┘  │    │  └────────────┘ │                     │
│  └─────────────────┘    └─────────────────┘                     │
└───────────────────────────────────────────────────────────────────┘
```

## Responsabilidades por Capa

### 📱 PRESENTATION Layer
**Responsabilidad**: Manejar la UI y la interacción del usuario

- **MainActivity**: Muestra la lista de mascotas usando RecyclerView
- **PetAdapter**: Renderiza cada item de mascota
- **PetViewModel**: 
  - Expone datos a la UI mediante StateFlow/LiveData
  - Coordina casos de uso
  - Transforma datos para presentación
  - Maneja el ciclo de vida de la UI

### 🎯 DOMAIN Layer
**Responsabilidad**: Lógica de negocio independiente de frameworks

- **Pet (Model)**: Modelo de dominio puro (sin dependencias de Android)
- **GetPetsUseCase**: Encapsula la lógica para obtener mascotas
- **AddPetUseCase**: Encapsula la lógica para agregar mascotas

**Ventajas**:
- Reutilizable
- Testeable
- Sin dependencias de Android
- Define las reglas del negocio

### 💾 DATA Layer
**Responsabilidad**: Gestión de fuentes de datos

#### Repository Pattern
- **PetRepository**: 
  - Abstrae las fuentes de datos
  - Decide cuándo usar cache local vs API remota
  - Transforma DTOs/Entities a Models de dominio

#### Local Data Source (Room)
- **AppDatabase**: Configuración de la base de datos
- **PetDao**: Queries SQL type-safe
- **PetEntity**: Representación en base de datos

#### Remote Data Source (Retrofit)
- **PetApiService**: Definición de endpoints
- **PetDto**: Objeto de transferencia desde API
- **RetrofitClient**: Configuración de red

## Flujo de Datos Típico

### 1️⃣ Obtener Mascotas
```
MainActivity → PetViewModel → GetPetsUseCase → PetRepository 
→ PetDao → Room Database → [Flow] → ViewModel → MainActivity
```

### 2️⃣ Agregar Mascota
```
MainActivity → PetViewModel → AddPetUseCase → PetRepository 
→ PetDao → Room Database → Success/Error → ViewModel → MainActivity
```

### 3️⃣ Sincronizar desde API
```
ViewModel → Repository → PetApiService → API Server
→ [Download] → Transform DTO to Entity → PetDao → Room Database
→ [Flow notifica cambios] → ViewModel → UI actualizada
```

## Inyección de Dependencias

### AppModule (DI Manual)
```kotlin
AppModule
├── provideDatabase()
├── providePetDao()
├── providePetApiService()
├── providePetRepository()
├── provideGetPetsUseCase()
└── provideAddPetUseCase()
```

## Ventajas de esta Arquitectura

✅ **Separación de Responsabilidades**: Cada capa tiene un propósito claro
✅ **Testeable**: Fácil mockear dependencias
✅ **Mantenible**: Cambios localizados por capa
✅ **Escalable**: Fácil agregar nuevas features
✅ **Independiente de Framework**: Domain layer puro
✅ **Single Source of Truth**: Repository decide la fuente de datos
✅ **Reactive**: Flow/LiveData para actualizaciones automáticas

## Convenciones de Nombres

- **Entity**: Sufijo para clases de Room (`PetEntity`)
- **Dto**: Sufijo para objetos de API (`PetDto`)
- **Model**: Sin sufijo para dominio (`Pet`)
- **UseCase**: Sufijo para casos de uso (`GetPetsUseCase`)
- **Repository**: Sufijo para repos (`PetRepository`)
- **ViewModel**: Sufijo para VMs (`PetViewModel`)
- **Adapter**: Sufijo para adapters (`PetAdapter`)
