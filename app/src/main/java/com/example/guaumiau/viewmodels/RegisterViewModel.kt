package com.example.guaumiau.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guaumiau.data.PetRegistration
import com.example.guaumiau.data.PetType
import com.example.guaumiau.data.Validator
import com.example.guaumiau.data.model.UserEntity
import com.example.guaumiau.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Registro
 */
class RegisterViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    // Campos de usuario
    fun onFullNameChange(name: String) {
        _uiState.value = _uiState.value.copy(fullName = name)
        validateFullName(name)
    }

    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
        validateEmail(email)
    }

    fun onPasswordChange(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
        validatePassword(password)
        // Re-validar confirmación si ya fue ingresada
        if (_uiState.value.passwordConfirmation.isNotBlank()) {
            validatePasswordConfirmation(password, _uiState.value.passwordConfirmation)
        }
    }

    fun onPasswordConfirmationChange(confirmation: String) {
        _uiState.value = _uiState.value.copy(passwordConfirmation = confirmation)
        validatePasswordConfirmation(_uiState.value.password, confirmation)
    }

    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone)
        validatePhone(phone)
    }

    // Validaciones en tiempo real
    private fun validateFullName(name: String) {
        val result = Validator.validateFullName(name)
        _uiState.value = _uiState.value.copy(fullNameError = result.errorMessage)
    }

    private fun validateEmail(email: String) {
        val result = Validator.validateEmail(email)
        _uiState.value = _uiState.value.copy(emailError = result.errorMessage)
    }

    private fun validatePassword(password: String) {
        val result = Validator.validatePassword(password)
        _uiState.value = _uiState.value.copy(passwordError = result.errorMessage)
    }

    private fun validatePasswordConfirmation(password: String, confirmation: String) {
        val result = Validator.validatePasswordConfirmation(password, confirmation)
        _uiState.value = _uiState.value.copy(passwordConfirmationError = result.errorMessage)
    }

    private fun validatePhone(phone: String) {
        val result = Validator.validatePhone(phone)
        _uiState.value = _uiState.value.copy(phoneError = result.errorMessage)
    }

    // Gestión de mascotas
    fun onAddPetClick() {
        val currentPets = _uiState.value.pets.toMutableList()
        val newPet = PetRegistrationState(
            id = currentPets.size,
            name = "",
            type = null
        )
        currentPets.add(newPet)
        _uiState.value = _uiState.value.copy(pets = currentPets)
    }

    fun onRemovePetClick(petId: Int) {
        val currentPets = _uiState.value.pets.toMutableList()
        currentPets.removeAt(petId)
        // Re-indexar IDs
        val reindexedPets = currentPets.mapIndexed { index, pet ->
            pet.copy(id = index)
        }
        _uiState.value = _uiState.value.copy(pets = reindexedPets)
    }

    fun onPetNameChange(petId: Int, name: String) {
        val currentPets = _uiState.value.pets.toMutableList()
        currentPets[petId] = currentPets[petId].copy(name = name)
        _uiState.value = _uiState.value.copy(pets = currentPets)
        validatePetName(petId, name)
    }

    fun onPetTypeChange(petId: Int, type: PetType) {
        val currentPets = _uiState.value.pets.toMutableList()
        currentPets[petId] = currentPets[petId].copy(type = type)
        _uiState.value = _uiState.value.copy(pets = currentPets)
        validatePetType(petId, type)
    }

    private fun validatePetName(petId: Int, name: String) {
        val result = Validator.validatePetName(name)
        val currentPets = _uiState.value.pets.toMutableList()
        currentPets[petId] = currentPets[petId].copy(nameError = result.errorMessage)
        _uiState.value = _uiState.value.copy(pets = currentPets)
    }

    private fun validatePetType(petId: Int, type: PetType?) {
        val result = Validator.validatePetType(type)
        val currentPets = _uiState.value.pets.toMutableList()
        currentPets[petId] = currentPets[petId].copy(typeError = result.errorMessage)
        _uiState.value = _uiState.value.copy(pets = currentPets)
    }

    // Registro
    fun onRegisterClick() {
        // Validar todos los campos
        if (!validateAllFields()) {
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val state = _uiState.value
                
                // Verificar si el email ya está registrado
                if (userRepository.isEmailRegistered(state.email)) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Este correo ya está registrado"
                    )
                    return@launch
                }
                
                // Crear el usuario
                val newUser = UserEntity(
                    fullName = state.fullName,
                    email = state.email,
                    password = state.password, // En producción, hashear la contraseña
                    phone = state.phone.takeIf { it.isNotBlank() }
                )
                
                // Guardar en la base de datos
                val userId = userRepository.registerUser(newUser)
                
                if (userId != null) {
                    // Registro exitoso
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        registerSuccess = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Error al registrar el usuario"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error: ${e.message}"
                )
            }
        }
    }

    private fun validateAllFields(): Boolean {
        val state = _uiState.value

        val fullNameValid = Validator.validateFullName(state.fullName)
        val emailValid = Validator.validateEmail(state.email)
        val passwordValid = Validator.validatePassword(state.password)
        val passwordConfValid = Validator.validatePasswordConfirmation(state.password, state.passwordConfirmation)
        val phoneValid = Validator.validatePhone(state.phone)

        // Validar mascotas
        var petsValid = true
        val updatedPets = state.pets.map { pet ->
            val nameValid = Validator.validatePetName(pet.name)
            val typeValid = Validator.validatePetType(pet.type)
            
            if (!nameValid.isValid || !typeValid.isValid) {
                petsValid = false
            }

            pet.copy(
                nameError = nameValid.errorMessage,
                typeError = typeValid.errorMessage
            )
        }

        _uiState.value = state.copy(
            fullNameError = fullNameValid.errorMessage,
            emailError = emailValid.errorMessage,
            passwordError = passwordValid.errorMessage,
            passwordConfirmationError = passwordConfValid.errorMessage,
            phoneError = phoneValid.errorMessage,
            pets = updatedPets
        )

        return fullNameValid.isValid && 
               emailValid.isValid && 
               passwordValid.isValid && 
               passwordConfValid.isValid && 
               phoneValid.isValid && 
               petsValid
    }
}

/**
 * Estado de UI para Registro
 */
data class RegisterUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val phone: String = "",
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmationError: String? = null,
    val phoneError: String? = null,
    val pets: List<PetRegistrationState> = emptyList(),
    val isLoading: Boolean = false,
    val registerSuccess: Boolean = false,
    val errorMessage: String? = null
)

/**
 * Estado para cada registro de mascota
 */
data class PetRegistrationState(
    val id: Int,
    val name: String,
    val type: PetType?,
    val nameError: String? = null,
    val typeError: String? = null
)
