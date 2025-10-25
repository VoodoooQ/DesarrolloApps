package com.example.guaumiau.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Repositorio simplificado para mascotas
 */
class PetRepository {
    
    private val petsList = mutableListOf<Pet>()
    
    /**
     * Obtiene todas las mascotas
     */
    fun getAllPets(): Flow<List<Pet>> = flow {
        emit(petsList.toList())
    }
    
    /**
     * Agrega una nueva mascota
     */
    suspend fun addPet(pet: Pet) {
        petsList.add(pet)
    }
    
    /**
     * Obtiene mascota por ID
     */
    suspend fun getPetById(id: Int): Pet? {
        return petsList.find { it.id == id }
    }
    
    /**
     * Elimina una mascota
     */
    suspend fun deletePet(pet: Pet) {
        petsList.remove(pet)
    }
}
