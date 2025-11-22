package com.example.guaumiau.data.remote

import com.example.guaumiau.data.remote.dto.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

/**
 * Servicio API para el microservicio de mascotas desplegado en Railway
 * 
 * Base URL: https://microservicedm-production.up.railway.app
 * 
 * Compatible con Android Studio Iguana 2023.1.1 Patch 2
 * Retrofit 2.9.0 (estable y probado)
 */
interface RailwayApiService {
    
    // ==================== ENDPOINTS DE MASCOTAS ====================
    
    /**
     * Obtiene todas las mascotas
     * GET /api/pets
     * 
     * @return Lista de todas las mascotas registradas en el sistema
     */
    @GET("api/pets")
    fun getAllPets(): Call<List<PetDto>>
    
    /**
     * Obtiene una mascota por ID
     * GET /api/pets/{id}
     * 
     * @param id Identificador único de la mascota
     * @return Datos de la mascota si existe
     */
    @GET("api/pets/{id}")
    fun getPetById(@Path("id") id: Int): Call<PetDto>
    
    /**
     * Obtiene todas las mascotas de un usuario específico
     * GET /api/pets?userEmail={email}
     * 
     * @param userEmail Email del usuario dueño de las mascotas
     * @return Lista de mascotas del usuario
     */
    @GET("api/pets")
    fun getPetsByUser(@Query("userEmail") userEmail: String): Call<List<PetDto>>
    
    /**
     * Crea una nueva mascota
     * POST /api/pets
     * 
     * @param pet Datos de la mascota a crear
     * @return Mascota creada con ID asignado
     */
    @POST("api/pets")
    fun createPet(@Body pet: CreatePetRequest): Call<PetDto>
    
    /**
     * Actualiza una mascota existente
     * PUT /api/pets/{id}
     * 
     * @param id Identificador de la mascota a actualizar
     * @param pet Nuevos datos de la mascota
     * @return Mascota actualizada
     */
    @PUT("api/pets/{id}")
    fun updatePet(
        @Path("id") id: Int,
        @Body pet: UpdatePetRequest
    ): Call<PetDto>
    
    /**
     * Elimina una mascota por ID
     * DELETE /api/pets/{id}
     * 
     * @param id Identificador de la mascota a eliminar
     * @return Respuesta sin contenido (204 No Content)
     */
    @DELETE("api/pets/{id}")
    fun deletePet(@Path("id") id: Int): Call<Void>
    
    // ==================== ENDPOINTS DE USUARIOS (opcionales) ====================
    
    /**
     * Registra un nuevo usuario
     * POST /api/users/register
     * 
     * @param user Datos del usuario a registrar
     * @return Usuario registrado
     */
    @POST("api/users/register")
    fun registerUser(@Body user: RegisterUserRequest): Call<UserDto>
    
    /**
     * Autenticación de usuario
     * POST /api/users/login
     * 
     * @param credentials Email y contraseña
     * @return Respuesta de autenticación con datos del usuario
     */
    @POST("api/users/login")
    fun loginUser(@Body credentials: LoginRequest): Call<AuthResponse>
    
    /**
     * Obtiene información de un usuario por email
     * GET /api/users/{email}
     * 
     * @param email Email del usuario
     * @return Datos del usuario
     */
    @GET("api/users/{email}")
    fun getUserByEmail(@Path("email") email: String): Call<UserDto>
    
    /**
     * Actualiza información del usuario
     * PUT /api/users/{email}
     * 
     * @param email Email del usuario a actualizar
     * @param user Nuevos datos del usuario
     * @return Usuario actualizado
     */
    @PUT("api/users/{email}")
    fun updateUser(
        @Path("email") email: String,
        @Body user: UserDto
    ): Call<UserDto>
}
