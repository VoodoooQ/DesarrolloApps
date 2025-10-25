package com.example.guaumiau.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.guaumiau.data.model.UserEntity

/**
 * DAO para operaciones de usuarios en la base de datos
 */
@Dao
interface UserDao {
    
    /**
     * Inserta un nuevo usuario en la base de datos
     * @return El ID del usuario insertado
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long
    
    /**
     * Busca un usuario por email
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?
    
    /**
     * Verifica si existe un usuario con el email dado
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailRegistered(email: String): Boolean
    
    /**
     * Autentica un usuario con email y contraseña
     */
    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun authenticate(email: String, password: String): UserEntity?
    
    /**
     * Obtiene todos los usuarios (para debugging)
     */
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>
}
