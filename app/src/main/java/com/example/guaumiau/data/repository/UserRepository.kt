package com.example.guaumiau.data.repository

import com.example.guaumiau.data.local.UserDao
import com.example.guaumiau.data.model.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Repositorio para gestionar operaciones de usuarios
 * Abstrae la capa de datos de la lógica de negocio
 */
class UserRepository(private val userDao: UserDao) {
    
    /**
     * Registra un nuevo usuario en la base de datos
     * @return ID del usuario registrado o null si el email ya existe
     */
    suspend fun registerUser(user: UserEntity): Long? = withContext(Dispatchers.IO) {
        try {
            // Verificar si el email ya está registrado
            if (userDao.isEmailRegistered(user.email)) {
                return@withContext null
            }
            userDao.insertUser(user)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Autentica un usuario con email y contraseña
     * @return UserEntity si las credenciales son correctas, null en caso contrario
     */
    suspend fun authenticateUser(email: String, password: String): UserEntity? = 
        withContext(Dispatchers.IO) {
            userDao.authenticate(email, password)
        }
    
    /**
     * Verifica si un email ya está registrado
     */
    suspend fun isEmailRegistered(email: String): Boolean = 
        withContext(Dispatchers.IO) {
            userDao.isEmailRegistered(email)
        }
    
    /**
     * Obtiene un usuario por email
     */
    suspend fun getUserByEmail(email: String): UserEntity? = 
        withContext(Dispatchers.IO) {
            userDao.getUserByEmail(email)
        }
}
