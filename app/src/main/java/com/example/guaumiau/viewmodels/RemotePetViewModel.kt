package com.example.guaumiau.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guaumiau.data.model.PetEntity
import com.example.guaumiau.data.repository.RemotePetRepository
import com.example.guaumiau.model.PetType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * ViewModel para gestión de mascotas con Railway API
 * 
 * Funcionalidades:
 * - Listar mascotas del usuario (sincronizadas desde Railway)
 * - Crear nuevas mascotas (POST a Railway)
 * - Eliminar mascotas (DELETE en Railway)
 * - Manejo robusto de errores de red
 * - Logging detallado para debugging
 * 
 * Compatible con Android Studio Iguana 2023.1.1 Patch 2
 */
class RemotePetViewModel(
    private val repository: RemotePetRepository,
    private val currentUserEmail: String
) : ViewModel() {
    
    companion object {
        private const val TAG = "RemotePetViewModel"
    }
    
    private val _uiState = MutableStateFlow(RemotePetUiState())
    val uiState: StateFlow<RemotePetUiState> = _uiState.asStateFlow()
    
    init {
        Log.d(TAG, "Inicializando ViewModel para usuario: $currentUserEmail")
        loadPetsFromLocal()
        syncPetsFromRemote()
    }
    
    // ==================== CARGA DE DATOS ====================
    
    /**
     * Carga mascotas desde Room (cache local)
     * Respuesta inmediata sin latencia de red
     */
    private fun loadPetsFromLocal() {
        Log.d(TAG, "Cargando mascotas desde Room")
        
        viewModelScope.launch {
            repository.getPetsByUserLocal(currentUserEmail).collect { pets ->
                Log.d(TAG, "Mascotas en Room: ${pets.size}")
                _uiState.value = _uiState.value.copy(
                    pets = pets,
                    isLoading = false
                )
            }
        }
    }
    
    /**
     * Sincroniza mascotas desde Railway API
     * Actualiza Room con datos del servidor
     */
    fun syncPetsFromRemote() {
        Log.d(TAG, "Sincronizando mascotas desde Railway")
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isSyncing = true,
                errorMessage = null
            )
            
            val result = repository.syncPetsFromRemote(currentUserEmail)
            
            result.fold(
                onSuccess = { pets ->
                    Log.d(TAG, "Sincronización exitosa: ${pets.size} mascotas")
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        successMessage = "Sincronización exitosa: ${pets.size} mascotas"
                    )
                },
                onFailure = { error ->
                    val errorMsg = when (error) {
                        is IOException -> "Error de red: ${error.message}"
                        else -> "Error: ${error.message}"
                    }
                    Log.e(TAG, "Error en sincronización: $errorMsg", error)
                    _uiState.value = _uiState.value.copy(
                        isSyncing = false,
                        errorMessage = errorMsg
                    )
                }
            )
        }
    }
    
    // ==================== CREACIÓN DE MASCOTAS ====================
    
    /**
     * Muestra diálogo para agregar nueva mascota
     */
    fun showAddPetDialog() {
        Log.d(TAG, "Mostrando diálogo de agregar mascota")
        _uiState.value = _uiState.value.copy(showAddPetDialog = true)
    }
    
    /**
     * Cierra diálogo de agregar mascota
     */
    fun dismissAddPetDialog() {
        Log.d(TAG, "Cerrando diálogo de agregar mascota")
        _uiState.value = _uiState.value.copy(
            showAddPetDialog = false,
            newPetName = "",
            newPetType = null,
            newPetNameError = null
        )
    }
    
    /**
     * Actualiza nombre de nueva mascota
     */
    fun onNewPetNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            newPetName = name,
            newPetNameError = null
        )
    }
    
    /**
     * Actualiza tipo de nueva mascota
     */
    fun onNewPetTypeChange(type: PetType) {
        _uiState.value = _uiState.value.copy(newPetType = type)
    }
    
    /**
     * Crea una nueva mascota en Railway y Room
     */
    fun createPet() {
        val state = _uiState.value
        
        // Validación
        if (state.newPetName.isBlank()) {
            _uiState.value = state.copy(newPetNameError = "El nombre es obligatorio")
            return
        }
        
        if (state.newPetName.length < 2) {
            _uiState.value = state.copy(newPetNameError = "Mínimo 2 caracteres")
            return
        }
        
        if (state.newPetType == null) {
            _uiState.value = state.copy(errorMessage = "Selecciona un tipo de mascota")
            return
        }
        
        Log.d(TAG, "Creando mascota: ${state.newPetName} (${state.newPetType.name})")
        
        viewModelScope.launch {
            _uiState.value = state.copy(isAddingPet = true, errorMessage = null)
            
            val newPet = PetEntity(
                name = state.newPetName.trim(),
                type = state.newPetType.name,
                userEmail = currentUserEmail
            )
            
            val result = repository.createPetRemote(newPet)
            
            result.fold(
                onSuccess = { petId ->
                    Log.d(TAG, "Mascota creada con ID: $petId")
                    _uiState.value = _uiState.value.copy(
                        isAddingPet = false,
                        showAddPetDialog = false,
                        newPetName = "",
                        newPetType = null,
                        successMessage = "Mascota '${newPet.name}' creada exitosamente"
                    )
                },
                onFailure = { error ->
                    val errorMsg = when (error) {
                        is IOException -> "Error de red: ${error.message}"
                        else -> "Error al crear mascota: ${error.message}"
                    }
                    Log.e(TAG, errorMsg, error)
                    _uiState.value = _uiState.value.copy(
                        isAddingPet = false,
                        errorMessage = errorMsg
                    )
                }
            )
        }
    }
    
    // ==================== ELIMINACIÓN DE MASCOTAS ====================
    
    /**
     * Muestra confirmación para eliminar mascota
     */
    fun showDeleteConfirmation(pet: PetEntity) {
        Log.d(TAG, "Mostrando confirmación de eliminación: ${pet.name}")
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = true,
            petToDelete = pet
        )
    }
    
    /**
     * Cancela eliminación de mascota
     */
    fun dismissDeleteDialog() {
        Log.d(TAG, "Cancelando eliminación")
        _uiState.value = _uiState.value.copy(
            showDeleteDialog = false,
            petToDelete = null
        )
    }
    
    /**
     * Confirma y ejecuta eliminación de mascota
     */
    fun confirmDeletePet() {
        val pet = _uiState.value.petToDelete ?: return
        
        Log.d(TAG, "Eliminando mascota: ${pet.name} (ID: ${pet.id})")
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingPet = true, errorMessage = null)
            
            val result = repository.deletePetRemote(pet.id)
            
            result.fold(
                onSuccess = {
                    Log.d(TAG, "Mascota eliminada exitosamente")
                    _uiState.value = _uiState.value.copy(
                        isDeletingPet = false,
                        showDeleteDialog = false,
                        petToDelete = null,
                        successMessage = "Mascota '${pet.name}' eliminada"
                    )
                },
                onFailure = { error ->
                    val errorMsg = when (error) {
                        is IOException -> "Error de red: ${error.message}"
                        else -> "Error al eliminar: ${error.message}"
                    }
                    Log.e(TAG, errorMsg, error)
                    _uiState.value = _uiState.value.copy(
                        isDeletingPet = false,
                        errorMessage = errorMsg
                    )
                }
            )
        }
    }
    
    // ==================== MENSAJES ====================
    
    /**
     * Limpia mensaje de éxito
     */
    fun clearSuccessMessage() {
        _uiState.value = _uiState.value.copy(successMessage = null)
    }
    
    /**
     * Limpia mensaje de error
     */
    fun clearErrorMessage() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

/**
 * Estado de UI para gestión de mascotas con Railway
 */
data class RemotePetUiState(
    // Lista de mascotas
    val pets: List<PetEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    
    // Agregar mascota
    val showAddPetDialog: Boolean = false,
    val newPetName: String = "",
    val newPetType: PetType? = null,
    val newPetNameError: String? = null,
    val isAddingPet: Boolean = false,
    
    // Eliminar mascota
    val showDeleteDialog: Boolean = false,
    val petToDelete: PetEntity? = null,
    val isDeletingPet: Boolean = false,
    
    // Mensajes
    val errorMessage: String? = null,
    val successMessage: String? = null
)
