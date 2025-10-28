package com.example.guaumiau.data.repository

import com.example.guaumiau.data.local.PetDao
import com.example.guaumiau.data.model.PetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repositorio para gestionar operaciones de mascotas
 */
class PetRepository(private val petDao: PetDao) {
    
    /**
     * Obtiene todas las mascotas de un usuario como Flow
     */
    fun getPetsByUser(userEmail: String): Flow<List<PetEntity>> {
        return petDao.getPetsByUser(userEmail)
    }
    
    /**
     * Obtiene todas las mascotas de un usuario de forma síncrona
     */
    suspend fun getPetsByUserSync(userEmail: String): List<PetEntity> = 
        withContext(Dispatchers.IO) {
            petDao.getPetsByUserSync(userEmail)
        }
    
    /**
     * Agrega una nueva mascota
     */
    suspend fun addPet(pet: PetEntity): Long = withContext(Dispatchers.IO) {
        petDao.insertPet(pet)
    }
    
    /**
     * Elimina una mascota
     */
    suspend fun deletePet(pet: PetEntity) = withContext(Dispatchers.IO) {
        petDao.deletePet(pet)
    }
    
    /**
     * Elimina una mascota por ID
     */
    suspend fun deletePetById(petId: Int) = withContext(Dispatchers.IO) {
        petDao.deletePetById(petId)
    }
    
    /**
     * Obtiene una mascota por ID
     */
    suspend fun getPetById(petId: Int): PetEntity? = withContext(Dispatchers.IO) {
        petDao.getPetById(petId)
    }
}
