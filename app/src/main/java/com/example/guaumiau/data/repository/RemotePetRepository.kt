package com.example.guaumiau.data.repository

import android.util.Log
import com.example.guaumiau.data.local.PetDao
import com.example.guaumiau.data.model.PetEntity
import com.example.guaumiau.data.remote.RailwayRetrofitClient
import com.example.guaumiau.data.remote.dto.CreatePetRequest
import com.example.guaumiau.data.remote.dto.PetDto
import com.example.guaumiau.data.remote.dto.UpdatePetRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException

/**
 * Repositorio para gestionar mascotas con sincronización local (Room) y remota (Railway)
 * 
 * Estrategia:
 * 1. Cache-First: Lee primero de Room (rápido)
 * 2. Sync-on-Write: Escribe en Railway y luego actualiza Room
 * 3. Error Handling: Manejo robusto de errores de red
 * 
 * Compatible con Android Studio Iguana 2023.1.1 Patch 2
 */
class RemotePetRepository(private val petDao: PetDao) {
    
    companion object {
        private const val TAG = "RemotePetRepository"
    }
    
    // ==================== OPERACIONES DE LECTURA ====================
    
    /**
     * Obtiene mascotas del usuario desde la base de datos local (Room)
     * Flow para observación reactiva de cambios
     * 
     * @param userEmail Email del usuario
     * @return Flow de lista de mascotas (se actualiza automáticamente)
     */
    fun getPetsByUserLocal(userEmail: String): Flow<List<PetEntity>> {
        Log.d(TAG, "getPetsByUserLocal: $userEmail")
        return petDao.getPetsByUser(userEmail)
    }
    
    /**
     * Sincroniza mascotas desde Railway al almacenamiento local
     * Descarga mascotas del servidor y actualiza Room
     * 
     * @param userEmail Email del usuario
     * @return Result con lista de mascotas o error
     */
    suspend fun syncPetsFromRemote(userEmail: String): Result<List<PetEntity>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Sincronizando mascotas desde Railway para: $userEmail")
            
            // Llamada al API de Railway
            val response = RailwayRetrofitClient.api.getPetsByUser(userEmail).execute()
            
            if (response.isSuccessful) {
                val remotePets = response.body() ?: emptyList()
                Log.d(TAG, "Mascotas obtenidas de Railway: ${remotePets.size}")
                
                // Convertir DTOs a entidades Room
                val localPets = remotePets.map { dto ->
                    PetEntity(
                        id = dto.id ?: 0,
                        name = dto.name,
                        type = dto.type,
                        userEmail = dto.userEmail
                    )
                }
                
                // Guardar en Room
                localPets.forEach { pet ->
                    petDao.insertPet(pet)
                }
                
                Log.d(TAG, "Sincronización exitosa: ${localPets.size} mascotas")
                Result.success(localPets)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                Log.e(TAG, "Error HTTP ${response.code()}: $errorBody")
                Result.failure(IOException("Error HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al sincronizar mascotas", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado al sincronizar mascotas", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene todas las mascotas desde Railway (sin filtro de usuario)
     * Útil para listados globales
     * 
     * @return Result con lista de mascotas o error
     */
    suspend fun getAllPetsFromRemote(): Result<List<PetDto>> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Obteniendo todas las mascotas desde Railway")
            
            val response = RailwayRetrofitClient.api.getAllPets().execute()
            
            if (response.isSuccessful) {
                val pets = response.body() ?: emptyList()
                Log.d(TAG, "Mascotas obtenidas: ${pets.size}")
                Result.success(pets)
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al obtener mascotas", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(e)
        }
    }
    
    /**
     * Obtiene una mascota por ID desde Railway
     * 
     * @param petId ID de la mascota
     * @return Result con mascota o error
     */
    suspend fun getPetByIdFromRemote(petId: Int): Result<PetDto> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Obteniendo mascota $petId desde Railway")
            
            val response = RailwayRetrofitClient.api.getPetById(petId).execute()
            
            if (response.isSuccessful) {
                val pet = response.body()
                if (pet != null) {
                    Log.d(TAG, "Mascota obtenida: ${pet.name}")
                    Result.success(pet)
                } else {
                    Result.failure(IOException("Mascota no encontrada"))
                }
            } else {
                handleErrorResponse(response)
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al obtener mascota", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(e)
        }
    }
    
    // ==================== OPERACIONES DE ESCRITURA ====================
    
    /**
     * Crea una nueva mascota en Railway y luego en Room
     * 
     * @param pet Entidad de mascota a crear
     * @return Result con ID de la mascota creada o error
     */
    suspend fun createPetRemote(pet: PetEntity): Result<Int> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Creando mascota en Railway: ${pet.name}")
            
            val request = CreatePetRequest(
                name = pet.name,
                type = pet.type,
                userEmail = pet.userEmail
            )
            
            val response = RailwayRetrofitClient.api.createPet(request).execute()
            
            if (response.isSuccessful) {
                val createdPet = response.body()
                if (createdPet?.id != null) {
                    Log.d(TAG, "Mascota creada en Railway con ID: ${createdPet.id}")
                    
                    // Guardar en Room con el ID del servidor
                    val localPet = PetEntity(
                        id = createdPet.id,
                        name = createdPet.name,
                        type = createdPet.type,
                        userEmail = createdPet.userEmail
                    )
                    petDao.insertPet(localPet)
                    
                    Log.d(TAG, "Mascota guardada en Room con ID: ${createdPet.id}")
                    Result.success(createdPet.id)
                } else {
                    Result.failure(IOException("Respuesta sin ID"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                Log.e(TAG, "Error HTTP ${response.code()}: $errorBody")
                Result.failure(IOException("Error HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al crear mascota", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado al crear mascota", e)
            Result.failure(e)
        }
    }
    
    /**
     * Actualiza una mascota en Railway y Room
     * 
     * @param petId ID de la mascota a actualizar
     * @param name Nuevo nombre
     * @param type Nuevo tipo
     * @return Result exitoso o error
     */
    suspend fun updatePetRemote(petId: Int, name: String, type: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Actualizando mascota $petId en Railway")
            
            val request = UpdatePetRequest(name = name, type = type)
            val response = RailwayRetrofitClient.api.updatePet(petId, request).execute()
            
            if (response.isSuccessful) {
                val updatedPet = response.body()
                if (updatedPet != null) {
                    Log.d(TAG, "Mascota actualizada en Railway: ${updatedPet.name}")
                    
                    // Actualizar en Room
                    val localPet = petDao.getPetById(petId)
                    if (localPet != null) {
                        val updated = localPet.copy(name = name, type = type)
                        petDao.insertPet(updated)
                        Log.d(TAG, "Mascota actualizada en Room")
                    }
                    
                    Result.success(Unit)
                } else {
                    Result.failure(IOException("Respuesta vacía"))
                }
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                Log.e(TAG, "Error HTTP ${response.code()}: $errorBody")
                Result.failure(IOException("Error HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al actualizar mascota", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(e)
        }
    }
    
    /**
     * Elimina una mascota en Railway y Room
     * 
     * @param petId ID de la mascota a eliminar
     * @return Result exitoso o error
     */
    suspend fun deletePetRemote(petId: Int): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Eliminando mascota $petId en Railway")
            
            val response = RailwayRetrofitClient.api.deletePet(petId).execute()
            
            if (response.isSuccessful || response.code() == 204) {
                Log.d(TAG, "Mascota eliminada en Railway")
                
                // Eliminar de Room
                petDao.deletePetById(petId)
                Log.d(TAG, "Mascota eliminada de Room")
                
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Sin detalles"
                Log.e(TAG, "Error HTTP ${response.code()}: $errorBody")
                Result.failure(IOException("Error HTTP ${response.code()}: $errorBody"))
            }
        } catch (e: IOException) {
            Log.e(TAG, "Error de red al eliminar mascota", e)
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado", e)
            Result.failure(e)
        }
    }
    
    // ==================== OPERACIONES LOCALES (Solo Room) ====================
    
    /**
     * Agrega mascota solo en Room (sin sincronizar con Railway)
     * Útil para modo offline
     */
    suspend fun addPetLocal(pet: PetEntity): Long = withContext(Dispatchers.IO) {
        Log.d(TAG, "Agregando mascota solo en Room: ${pet.name}")
        petDao.insertPet(pet)
    }
    
    /**
     * Elimina mascota solo de Room
     */
    suspend fun deletePetLocal(petId: Int) = withContext(Dispatchers.IO) {
        Log.d(TAG, "Eliminando mascota $petId solo de Room")
        petDao.deletePetById(petId)
    }
    
    // ==================== UTILIDADES ====================
    
    /**
     * Maneja respuestas de error HTTP con logging detallado
     */
    private fun <T> handleErrorResponse(response: Response<T>): Result<T> {
        val errorBody = response.errorBody()?.string() ?: "Sin detalles de error"
        val errorMessage = when (response.code()) {
            400 -> "Petición inválida: $errorBody"
            401 -> "No autorizado"
            403 -> "Prohibido"
            404 -> "Recurso no encontrado"
            500 -> "Error interno del servidor"
            else -> "Error HTTP ${response.code()}: $errorBody"
        }
        Log.e(TAG, errorMessage)
        return Result.failure(IOException(errorMessage))
    }
}
