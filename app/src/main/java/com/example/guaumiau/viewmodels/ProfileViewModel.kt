package com.example.guaumiau.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guaumiau.data.PetType
import com.example.guaumiau.data.Validator
import com.example.guaumiau.data.model.PetEntity
import com.example.guaumiau.data.model.UserEntity
import com.example.guaumiau.data.repository.PetRepository
import com.example.guaumiau.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Perfil de Usuario
 */
class ProfileViewModel(
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val currentUserEmail: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserProfile()
        loadUserPets()
    }

    /**
     * Carga los datos del perfil del usuario actual
     */
    private fun loadUserProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            val user = userRepository.getUserByEmail(currentUserEmail)
            
            if (user != null) {
                _uiState.value = _uiState.value.copy(
                    user = user,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar el perfil"
                )
            }
        }
    }
    
    /**
     * Carga las mascotas del usuario
     */
    private fun loadUserPets() {
        viewModelScope.launch {
            petRepository.getPetsByUser(currentUserEmail).collect { pets ->
                _uiState.value = _uiState.value.copy(pets = pets)
            }
        }
    }

    /**
     * Manejo de cambios en formulario de cambio de contraseña
     */
    fun onCurrentPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            currentPassword = password,
            currentPasswordError = null
        )
    }

    fun onNewPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            newPassword = password,
            newPasswordError = null
        )
        
        // Re-validar confirmación si ya fue ingresada
        if (_uiState.value.confirmNewPassword.isNotBlank()) {
            validatePasswordConfirmation()
        }
    }

    fun onConfirmNewPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(
            confirmNewPassword = password,
            confirmNewPasswordError = null
        )
    }

    /**
     * Valida que la nueva contraseña sea válida
     */
    private fun validateNewPassword(): Boolean {
        val result = Validator.validatePassword(_uiState.value.newPassword)
        _uiState.value = _uiState.value.copy(
            newPasswordError = result.errorMessage
        )
        return result.isValid
    }

    /**
     * Valida que las contraseñas coincidan
     */
    private fun validatePasswordConfirmation(): Boolean {
        val result = Validator.validatePasswordConfirmation(
            _uiState.value.newPassword,
            _uiState.value.confirmNewPassword
        )
        _uiState.value = _uiState.value.copy(
            confirmNewPasswordError = result.errorMessage
        )
        return result.isValid
    }

    /**
     * Cambia la contraseña del usuario
     */
    fun changePassword() {
        // Validar campos
        if (_uiState.value.currentPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(
                currentPasswordError = "Ingresa tu contraseña actual"
            )
            return
        }

        if (!validateNewPassword()) return
        if (!validatePasswordConfirmation()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isChangingPassword = true)

            try {
                // Simular delay para mostrar loader
                kotlinx.coroutines.delay(800)
                
                // Verificar que la contraseña actual sea correcta
                val user = _uiState.value.user
                if (user == null) {
                    _uiState.value = _uiState.value.copy(
                        isChangingPassword = false,
                        errorMessage = "Error al cargar datos del usuario"
                    )
                    return@launch
                }

                if (user.password != _uiState.value.currentPassword) {
                    _uiState.value = _uiState.value.copy(
                        isChangingPassword = false,
                        currentPasswordError = "Contraseña actual incorrecta"
                    )
                    return@launch
                }

                // Actualizar contraseña
                val success = userRepository.updatePassword(
                    userEmail = user.email,
                    newPassword = _uiState.value.newPassword
                )

                if (success) {
                    _uiState.value = _uiState.value.copy(
                        isChangingPassword = false,
                        showPasswordChangeDialog = false,
                        currentPassword = "",
                        newPassword = "",
                        confirmNewPassword = "",
                        successMessage = "Contraseña actualizada correctamente"
                    )
                    // Recargar perfil
                    loadUserProfile()
                } else {
                    _uiState.value = _uiState.value.copy(
                        isChangingPassword = false,
                        errorMessage = "Error al actualizar la contraseña"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isChangingPassword = false,
                    errorMessage = "Error al cambiar contraseña: ${e.message}"
                )
            }
        }
    }

    /**
     * Muestra/oculta el diálogo de cambio de contraseña
     */
    fun togglePasswordChangeDialog() {
        _uiState.value = _uiState.value.copy(
            showPasswordChangeDialog = !_uiState.value.showPasswordChangeDialog,
            currentPassword = "",
            newPassword = "",
            confirmNewPassword = "",
            currentPasswordError = null,
            newPasswordError = null,
            confirmNewPasswordError = null
        )
    }

    /**
     * Limpia los mensajes de error/éxito
     */
    fun clearMessages() {
        _uiState.value = _uiState.value.copy(
            errorMessage = null,
            successMessage = null
        )
    }
    
    // ============ GESTIÓN DE MASCOTAS ============
    
    /**
     * Muestra/oculta el diálogo de agregar mascota
     */
    fun toggleAddPetDialog() {
        _uiState.value = _uiState.value.copy(
            showAddPetDialog = !_uiState.value.showAddPetDialog,
            newPetName = "",
            newPetType = null,
            newPetNameError = null
        )
    }
    
    /**
     * Actualiza el nombre de la nueva mascota
     */
    fun onNewPetNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            newPetName = name,
            newPetNameError = null
        )
    }
    
    /**
     * Actualiza el tipo de la nueva mascota
     */
    fun onNewPetTypeChange(type: PetType) {
        _uiState.value = _uiState.value.copy(newPetType = type)
    }
    
    /**
     * Agrega una nueva mascota
     */
    fun addPet() {
        val state = _uiState.value
        
        // Validar nombre
        if (state.newPetName.isBlank()) {
            _uiState.value = state.copy(
                newPetNameError = "El nombre es obligatorio"
            )
            return
        }
        
        if (state.newPetName.length < 2) {
            _uiState.value = state.copy(
                newPetNameError = "El nombre debe tener al menos 2 caracteres"
            )
            return
        }
        
        // Validar tipo
        if (state.newPetType == null) {
            _uiState.value = state.copy(
                errorMessage = "Debes seleccionar un tipo de mascota"
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = state.copy(isAddingPet = true)
            
            val newPet = PetEntity(
                name = state.newPetName.trim(),
                type = state.newPetType.name,
                userEmail = currentUserEmail
            )
            
            val petId = petRepository.addPet(newPet)
            
            if (petId > 0) {
                _uiState.value = _uiState.value.copy(
                    isAddingPet = false,
                    showAddPetDialog = false,
                    newPetName = "",
                    newPetType = null,
                    successMessage = "Mascota agregada correctamente"
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isAddingPet = false,
                    errorMessage = "Error al agregar mascota"
                )
            }
        }
    }
    
    /**
     * Elimina una mascota
     */
    fun deletePet(petId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingPet = true)
            
            try {
                // Simular delay para mostrar loader
                kotlinx.coroutines.delay(500)
                
                petRepository.deletePetById(petId)
                _uiState.value = _uiState.value.copy(
                    isDeletingPet = false,
                    successMessage = "Mascota eliminada"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeletingPet = false,
                    errorMessage = "Error al eliminar mascota"
                )
            }
        }
    }
}

/**
 * Estado de UI para la pantalla de Perfil
 */
data class ProfileUiState(
    val user: UserEntity? = null,
    val isLoading: Boolean = false,
    
    // Cambio de contraseña
    val showPasswordChangeDialog: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val isChangingPassword: Boolean = false,
    
    // Errores de contraseña
    val currentPasswordError: String? = null,
    val newPasswordError: String? = null,
    val confirmNewPasswordError: String? = null,
    
    // Mascotas
    val pets: List<PetEntity> = emptyList(),
    val showAddPetDialog: Boolean = false,
    val newPetName: String = "",
    val newPetType: PetType? = null,
    val newPetNameError: String? = null,
    val isAddingPet: Boolean = false,
    val isDeletingPet: Boolean = false,
    
    // Mensajes
    val errorMessage: String? = null,
    val successMessage: String? = null
)
