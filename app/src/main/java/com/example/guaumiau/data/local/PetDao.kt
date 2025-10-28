package com.example.guaumiau.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guaumiau.data.model.PetEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO para operaciones de mascotas en la base de datos
 */
@Dao
interface PetDao {
    
    /**
     * Inserta una nueva mascota
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPet(pet: PetEntity): Long
    
    /**
     * Obtiene todas las mascotas de un usuario
     */
    @Query("SELECT * FROM pets WHERE userEmail = :userEmail")
    fun getPetsByUser(userEmail: String): Flow<List<PetEntity>>
    
    /**
     * Obtiene todas las mascotas de un usuario (sin Flow)
     */
    @Query("SELECT * FROM pets WHERE userEmail = :userEmail")
    suspend fun getPetsByUserSync(userEmail: String): List<PetEntity>
    
    /**
     * Elimina una mascota
     */
    @Delete
    suspend fun deletePet(pet: PetEntity)
    
    /**
     * Elimina una mascota por ID
     */
    @Query("DELETE FROM pets WHERE id = :petId")
    suspend fun deletePetById(petId: Int)
    
    /**
     * Obtiene una mascota por ID
     */
    @Query("SELECT * FROM pets WHERE id = :petId LIMIT 1")
    suspend fun getPetById(petId: Int): PetEntity?
}
